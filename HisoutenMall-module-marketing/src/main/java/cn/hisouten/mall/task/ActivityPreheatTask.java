package cn.hisouten.mall.task;

import cn.hisouten.mall.mapper.SeckillActivityMapper;
import cn.hisouten.mall.mapper.SeckillProductMapper;
import cn.hisouten.mall.pojo.entity.SeckillActivity;
import cn.hisouten.mall.pojo.entity.SeckillProduct;
import cn.hisouten.mall.pojo.vo.activity.UserSeckillActivityDetailVO;
import cn.hisouten.mall.service.SeckillService;
import cn.hisouten.mall.util.CacheClientUtils;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static cn.hisouten.mall.constant.RedisConstant.*;

/**
 * 秒杀活动预热任务
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ActivityPreheatTask {

    private final SeckillProductMapper seckillProductMapper;
    private final SeckillActivityMapper seckillActivityMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final CacheClientUtils cacheClientUtils;
    private final SeckillService seckillService;

    /**
     * 每分钟扫描一次是否有秒杀活动需要预热进redis
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void preheatActivity(){
        log.info("扫描预热活动中……");
        List<SeckillProduct> list = seckillProductMapper.selectAboutToStart(5);
        Set<Long> activityIds = new HashSet<>();

        for (SeckillProduct sp : list) {
            String key = SECKILL_STOCK + sp.getSkuId();
            stringRedisTemplate.opsForValue().setIfAbsent(key, String.valueOf(sp.getSeckillStock()), 2, TimeUnit.HOURS);

            String limitKey = SECKILL_PRODUCT_LIMIT + sp.getSkuId();
            stringRedisTemplate.opsForValue().setIfAbsent(limitKey, String.valueOf(sp.getLimitPerUser()), 2, TimeUnit.HOURS);

            String skuSetKey = CACHE_SECKILL_SKU_SET_PREFIX + sp.getActivityId();
            stringRedisTemplate.opsForSet().add(skuSetKey, sp.getSkuId().toString());

            activityIds.add(sp.getActivityId());
        }

        //统一查活动信息，并写入 Redis
        for (Long activityId : activityIds) {
            // 先查 Redis，没有再去查库（防止重复查）
            String cacheKey = CACHE_SECKILL_ACTIVITY_INFO_PREFIX + activityId;
            Boolean absent = stringRedisTemplate.opsForValue().setIfAbsent(cacheKey, "1", 2, TimeUnit.HOURS);
            // 如果 absent 为 true，说明刚占位，去查库并写回真正的数据
            if (Boolean.TRUE.equals(absent)) {
                SeckillActivity activity = seckillActivityMapper.selectById(activityId);
                if (activity != null) {
                    stringRedisTemplate.opsForValue().set(cacheKey, JSONUtil.toJsonStr(activity), 2, TimeUnit.HOURS);
                } else {
                    stringRedisTemplate.delete(cacheKey); // 没查到把占位删了
                }
            }

            // 3. 预热活动详情
            UserSeckillActivityDetailVO detail = seckillService.loadDetailFromDB(activityId);
            if (detail != null) {
                cacheClientUtils.setWithLogic(
                        CACHE_SECKILL_PRODUCT_DETAIL_PREFIX + activityId,
                        detail,
                        CACHE_SECKILL_PRODUCT_DETAIL_TTL,
                        TimeUnit.MINUTES
                );
            }
        }
    }
}

