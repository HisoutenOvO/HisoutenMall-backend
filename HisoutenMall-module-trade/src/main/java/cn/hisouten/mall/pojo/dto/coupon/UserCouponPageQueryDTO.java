package cn.hisouten.mall.pojo.dto.coupon;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import lombok.Data;

/**
 * 用户分页查询已有优惠券参数
 */
@Data
public class UserCouponPageQueryDTO extends BasePageQueryDTO {
    private Integer status; //0-未使用 1-已使用 2-已过期
}
