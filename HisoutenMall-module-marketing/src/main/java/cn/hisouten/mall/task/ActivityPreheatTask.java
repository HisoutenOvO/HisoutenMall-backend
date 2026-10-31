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
    @Scheduled(cron = "0 * * * * ?")
    public void preheatActivity(){
        log.info("扫描预热活动中……");
        // 每分钟扫一次
        List<SeckillProduct> list = seckillProductMapper.selectAboutToStart(5);  // 5分钟内开始的活动
        Set<Long> activityIds = new HashSet<>();
        for (SeckillProduct sp : list) {
            String key = SECKILL_STOCK + sp.getSkuId();
            // 幂等：只有key不存在才写
            Boolean absent = stringRedisTemplate.opsForValue()
                    .setIfAbsent(key, String.valueOf(sp.getSeckillStock()), 2, TimeUnit.HOURS);
            if (Boolean.TRUE.equals(absent)) {
                log.info("[预热] skuId={}, stock={}", sp.getSkuId(), sp.getSeckillStock());
            }

            // 预热限购数
            String limitKey = SECKILL_PRODUCT_LIMIT + sp.getSkuId();
            stringRedisTemplate.opsForValue().setIfAbsent(limitKey, String.valueOf(sp.getLimitPerUser()), 2, TimeUnit.HOURS);

            activityIds.add(sp.getActivityId());

            // 把合法的skuId存入集合，这样秒杀时可以判断这个skuId到底属不属于这个活动
            String skuSetKey = CACHE_SECKILL_SKU_SET_PREFIX + sp.getActivityId();
            stringRedisTemplate.opsForSet().add(skuSetKey, sp.getSkuId().toString());

            // 预热活动相关
            for (Long activityId : activityIds) {
                //预热活动基础信息,用于秒杀接口校验时间
                SeckillActivity activity = seckillActivityMapper.selectById(activityId);
                if (activity != null) {
                    stringRedisTemplate.opsForValue().setIfAbsent(
                            CACHE_SECKILL_ACTIVITY_INFO_PREFIX + activityId,
                            JSONUtil.toJsonStr(activity),
                            2, TimeUnit.HOURS
                    );
                }
            }
        }

        // 预热活动详情
        for (Long activityId : activityIds) {
            UserSeckillActivityDetailVO detail = seckillService.loadDetailFromDB(activityId);
            if (detail == null) continue;
            cacheClientUtils.setWithLogic(
                    CACHE_SECKILL_PRODUCT_DETAIL_PREFIX + activityId,
                    detail,
                    CACHE_SECKILL_PRODUCT_DETAIL_TTL,
                    TimeUnit.MINUTES
            );
        }
    }
}

