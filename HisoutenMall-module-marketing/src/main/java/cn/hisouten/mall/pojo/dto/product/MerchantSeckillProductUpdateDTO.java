package cn.hisouten.mall.pojo.dto.product;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商家端修改秒杀商品参数
 */
@Data
public class MerchantSeckillProductUpdateDTO {

    private BigDecimal seckillPrice;

    private Integer seckillStock;

    private Integer limitPerUser;

}
