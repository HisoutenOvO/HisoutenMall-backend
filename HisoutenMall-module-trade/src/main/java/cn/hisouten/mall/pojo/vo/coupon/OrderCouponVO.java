package cn.hisouten.mall.pojo.vo.coupon;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 通用的用于在订单中展示优惠券的VO类
 */
@Data
@NoArgsConstructor
public class OrderCouponVO {
    private String couponName;

    private Integer type;          // 1-满减 2-折扣

    private BigDecimal value;
}
