package cn.hisouten.mall.pojo.vo.coupon;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户分页查询可领取的优惠券
 */
@Data
@NoArgsConstructor
public class AvailableCouponPageResultVO {

    private Long id;

    private String name;

    private Integer type;

    private BigDecimal value;

    private BigDecimal minAmount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer remain;         // 剩余可领数量

    private Boolean received;       // 当前用户是否已领过

    private String merchantName;

}
