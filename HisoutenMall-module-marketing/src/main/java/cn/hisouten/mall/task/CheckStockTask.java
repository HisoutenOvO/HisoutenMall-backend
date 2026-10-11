package cn.hisouten.mall.task;

import cn.hisouten.mall.mapper.SeckillProductMapper;
import cn.hisouten.mall.pojo.entity.ProductSku;
import cn.hisouten.mall.pojo.entity.SeckillProduct;
import cn.hisouten.mall.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static cn.hisouten.mall.constant.RedisConstant.SECKILL_STOCK;

/**
 * 秒杀活动redis与数据库库存对账任务
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CheckStockTask {

    private final SeckillProductMapper seckillProductMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final ProductService productService;

    /**
     * 每分钟对账一次
     */
    @Scheduled(cron = "0 * * * * ?")
    public void checkStock(){
        for (SeckillProduct sp : seckillProductMapper.selectAllActive()) {
            String key = SECKILL_STOCK + sp.getSkuId();
            String redisStock = stringRedisTemplate.opsForValue().get(key);
            if (redisStock == null) continue;

            ProductSku sku = productService.getSkuBySkuId(sp.getSkuId());
            int dbStock = sku == null ? 0 : sku.getStock();

            if (Integer.parseInt(redisStock) != dbStock) {
                log.warn("[对账] 不一致 skuId={}, Redis={}, DB={}", sp.getSkuId(), redisStock, dbStock);
                stringRedisTemplate.opsForValue().set(key, String.valueOf(dbStock));
            }
        }
    }
}
