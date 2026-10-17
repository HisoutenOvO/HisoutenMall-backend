package cn.hisouten.mall.pojo.dto.coupon;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商家修改优惠券参数
 */
@Data
public class MerchantCouponUpdateDTO {

    @NotBlank(message = "优惠券名称不能为空")
    @Size(max = 100, message = "优惠券名称不能超过100字")
    private String name;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
}