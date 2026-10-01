package cn.hisouten.mall.pojo.bo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用于中转订单数据的BO
 */
@Data
@Builder
public class OrderItemBO {
    // 商品信息
    private Long productId;

    private Long skuId;

    private String productName;

    private String skuSpecs;

    private String image;

    private BigDecimal price;

    private Integer quantity;

    private Integer stock;          // 用于校验

    // 商家信息
    private Long merchantId;

    private String merchantName;

    // 购物车来源（用于下单后清理）
    private Long cartItemId;
}
