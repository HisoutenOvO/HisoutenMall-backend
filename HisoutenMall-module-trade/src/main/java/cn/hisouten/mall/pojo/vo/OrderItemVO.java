package cn.hisouten.mall.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户查询订单项返回值
 */
@Data
public class OrderItemVO {

    private Long orderId;

    private Long productId;

    private Long skuId;

    private String productName;

    private String skuSpecs;

    private String image;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal totalPrice;
}
