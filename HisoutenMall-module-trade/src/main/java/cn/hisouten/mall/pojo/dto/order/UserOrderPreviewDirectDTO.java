package cn.hisouten.mall.pojo.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户直接购买预计算下单信息参数
 */
@Data
public class UserOrderPreviewDirectDTO {
    @NotNull(message = "商品SKU不能为空")
    private Long skuId;

    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量最少为1")
    private Integer quantity;

    private Long userCouponId;
}