package cn.hisouten.mall.pojo.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户创建订单参数
 */
@Data
public class UserOrderCreateDTO {

    @NotNull(message = "收货地址不能为空")
    private Long addressId;

    @NotNull(message = "商品SKU不能为空")
    private Long skuId;

    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量最少为1")
    private Integer quantity;

    // 允许为空，不使用优惠券
    private Long userCouponId;
}