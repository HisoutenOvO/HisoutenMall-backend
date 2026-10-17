package cn.hisouten.mall.pojo.dto.coupon;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商家新增优惠券参数
 */
@Data
public class MerchantCouponAddDTO {

    private String name;

    private Integer type;

    private BigDecimal value;

    private BigDecimal minAmount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer total;

}
