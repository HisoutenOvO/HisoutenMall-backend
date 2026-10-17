package cn.hisouten.mall.pojo.dto.coupon;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商家修改优惠券参数
 */
@Data
public class MerchantCouponUpdateDTO {
    private String name;

    private LocalDateTime endTime;
}
