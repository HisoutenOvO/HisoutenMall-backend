package cn.hisouten.mall.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户查询可用优惠券返回值
 */
@Data
@NoArgsConstructor
public class UserUsableCouponVO {

    private Long userCouponId;

    private String name;

    private Integer type;

    private BigDecimal value;

    private BigDecimal minAmount;

    private BigDecimal discountAmount;   // 本单能减多少钱

    private LocalDateTime expireTime;

}
