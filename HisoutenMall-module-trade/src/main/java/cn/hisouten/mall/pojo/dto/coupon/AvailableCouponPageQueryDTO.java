package cn.hisouten.mall.pojo.dto.coupon;

import cn.hisouten.mall.pojo.BasePageQueryDTO;
import lombok.Data;

/**
 * 用户分页查询可领取优惠券列表参数
 */
@Data
public class AvailableCouponPageQueryDTO extends BasePageQueryDTO {
    private Long merchant;
}
