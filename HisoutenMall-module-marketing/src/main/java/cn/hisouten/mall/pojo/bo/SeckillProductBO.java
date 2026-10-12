package cn.hisouten.mall.pojo.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 用于中转秒杀商品详情的BO类
 */
@Data
public class SeckillProductBO {
    private Long id;              // seckill_product.id

    private Long skuId;

    private String productName;   // 商品名

    private String skuSpecs;      // 规格

    private String image;

    private BigDecimal originalPrice;   // 原价（product_sku.price）

    private BigDecimal seckillPrice;    // 秒杀价

    private Integer seckillStock;       // 秒杀库存

    private Integer skuStock;           // 普通库存

    private Integer limitPerUser;       // 限购
}
