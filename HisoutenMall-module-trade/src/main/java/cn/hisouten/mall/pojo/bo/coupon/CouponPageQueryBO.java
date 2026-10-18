package cn.hisouten.mall.pojo.bo.coupon;

import lombok.Data;

/**
 * 用于中转优惠券分页查询参数的BO类
 */
@Data
public class CouponPageQueryBO {

    private String keyword;

    private Integer status;

    private Integer type;

    private String merchantName;

}
