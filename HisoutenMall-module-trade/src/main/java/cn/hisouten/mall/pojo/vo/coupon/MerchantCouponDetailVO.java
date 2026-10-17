package cn.hisouten.mall.pojo.vo.coupon;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商家端查询优惠券参数返回值
 */
@Data
@NoArgsConstructor
public class MerchantCouponDetailVO {

    private Long id;

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