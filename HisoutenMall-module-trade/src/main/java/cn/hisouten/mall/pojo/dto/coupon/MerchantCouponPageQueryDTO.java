package cn.hisouten.mall.pojo.dto.coupon;

import cn.hisouten.mall.pojo.BasePageQueryDTO;
import lombok.Data;

/**
 * 商家端分页查询优惠券参数
 */
@Data
public class MerchantCouponPageQueryDTO extends BasePageQueryDTO {
    private String keyword;

    private Integer status;

    private Integer type;
}
