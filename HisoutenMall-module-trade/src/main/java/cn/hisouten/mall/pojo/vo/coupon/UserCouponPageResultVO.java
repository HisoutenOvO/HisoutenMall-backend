package cn.hisouten.mall.pojo.vo.coupon;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户分页查询已有优惠券返回值
 */
@Data
public class UserCouponPageResultVO {

    private Long id;                // user_coupon.id

    private Long couponId;

    private String couponName;

    private Integer type;           // 1-满减 2-折扣

    private BigDecimal value;

    private BigDecimal minAmount;

    private Integer status;

    private LocalDateTime receiveTime;

    private LocalDateTime expireTime;

    private String merchantName;    // 店铺名

}