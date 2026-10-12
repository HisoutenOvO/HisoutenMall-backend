package cn.hisouten.mall.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用户端查询秒杀商品项详情返回值
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSeckillProductVO {

    private Long skuId;

    private String productName;

    private String skuSpecs;

    private String image;

    private BigDecimal originalPrice;

    private BigDecimal seckillPrice;

    private Integer seckillStock;    // 剩余库存（从 Redis 取实时值）

    private Integer limitPerUser;

}
