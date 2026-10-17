package cn.hisouten.mall.pojo.dto.coupon;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商家新增优惠券参数
 */
@Data
public class MerchantCouponAddDTO {

    @NotBlank(message = "优惠券名称不能为空")
    @Size(max = 100, message = "优惠券名称不能超过100字")
    private String name;

    @NotNull(message = "优惠券类型不能为空")
    private Integer type;

    @NotNull(message = "优惠值/折扣不能为空")
    @DecimalMin(value = "0.01", message = "优惠值必须大于0")
    private BigDecimal value;

    @NotNull(message = "使用门槛不能为空")
    @DecimalMin(value = "0.00", message = "使用门槛不能为负数")
    private BigDecimal minAmount;

    @NotNull(message = "开始时间不能为空")
    @Future(message = "开始时间必须在未来")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    @NotNull(message = "发放总量不能为空")
    @Min(value = 1, message = "发放总量最少为1")
    private Integer total;

}