package cn.hisouten.mall.pojo.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户从购物车创建订单的参数
 */
@Data
public class UserOrderFromCartDTO {

    @NotNull(message = "地址不能为空")
    private Long addressId;

    private Long userCouponId;
}
