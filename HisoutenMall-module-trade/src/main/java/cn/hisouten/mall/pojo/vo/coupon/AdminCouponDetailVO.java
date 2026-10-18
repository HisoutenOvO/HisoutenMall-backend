package cn.hisouten.mall.pojo.vo.coupon;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理员查询优惠券详情返回值
 */
@Data
@NoArgsConstructor
public class AdminCouponDetailVO {

    private Long id;

    private String merchantName;

    private String name;

    private Integer type;

    private BigDecimal value;

    private BigDecimal minAmount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer total;

    private Integer remain;

    private Integer status;

    private LocalDateTime createTime;
}
