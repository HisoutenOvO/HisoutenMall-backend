package cn.hisouten.mall.task;

import cn.hisouten.mall.mapper.SeckillActivityMapper;
import cn.hisouten.mall.mapper.SeckillProductMapper;
import cn.hisouten.mall.pojo.entity.SeckillActivity;
import cn.hisouten.mall.pojo.entity.SeckillProduct;
import cn.hisouten.mall.service.ProductService;
import cn.hisouten.mall.util.CacheClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static cn.hisouten.mall.constant.RedisConstant.*;
import static cn.hisouten.mall.constant.SeckillConstant.COMPLETED;
import static cn.hisouten.mall.constant.SeckillConstant.PENDING;

/**
 * 改变秒杀活动状态任务
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChangeActivityStatusTask {
    private final SeckillActivityMapper seckillActivityMapper;
    private final SeckillProductMapper seckillProductMapper;

    private final ProductService productService;

    private final StringRedisTemplate stringRedisTemplate;
    private final CacheManager cacheManager;
    private final CacheClientUtils cacheClientUtils;

    /**
     * 每分钟扫描一次是否有秒杀活动需要改变状态
     */
    @Transactional
    @Scheduled(cron = "0 * * * * ?")
    public void changeStatus() {



        LocalDateTime now = LocalDateTime.now();

        // 1. 处理已结束的活动
        List<SeckillActivity> endingActivities = seckillActivityMapper.selectEndingActivities(now);
        for (SeckillActivity activity : endingActivities) {
            // 拿到该活动下所有秒杀商品
            List<SeckillProduct> products = seckillProductMapper.selectByActivityId(activity.getId());

            // 处理每个商品
            for (SeckillProduct sp : products) {
                // 归还剩余库存到普通库存
                if (sp.getSeckillStock() > 0) {
                    productService.restoreStock(sp.getSkuId(), sp.getSeckillStock());
                }
                // 清理 Redis 缓存
                stringRedisTemplate.delete(SECKILL_STOCK + sp.getSkuId());
                stringRedisTemplate.delete(SECKILL_USERS + sp.getSkuId());
            }

            //清理活动详情缓存
            cacheClientUtils.delete(CACHE_SECKILL_PRODUCT_DETAIL_PREFIX, activity.getId());

            // 改活动状态
            activity.setStatus(COMPLETED);
            seckillActivityMapper.updateById(activity);
            //清理活动列表缓存
            Cache cache = cacheManager.getCache(CACHE_SECKILL_ACTIVITY_LIST);
            if (cache != null) {
                cache.clear();
            }
            log.info("[秒杀] 活动 {} 结束，处理 {} 个商品", activity.getName(), products.size());
        }

        // 2. 处理开始的活动
        int started = seckillActivityMapper.startActivities(now);

        if (started > PENDING || !endingActivities.isEmpty()) {
            log.info("[秒杀状态] 开始 {} 个，结束 {} 个", started, endingActivities.size());
        }
    }
}
