package cn.hisouten.mall.pojo.dto.coupon;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户分页查询可领取优惠券列表参数
 */
@Data
public class AvailableCouponPageQueryDTO extends BasePageQueryDTO {

    @NotNull(message = "商家ID不能为空")
    private Long merchant;
}