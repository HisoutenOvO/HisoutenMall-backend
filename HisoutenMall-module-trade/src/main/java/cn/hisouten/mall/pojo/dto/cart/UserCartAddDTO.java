package cn.hisouten.mall.pojo.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户购物车新增参数
 */
@Data
public class UserCartAddDTO {

    @NotNull(message = "SKU ID不能为空")
    @Min(value = 1, message = "SKU ID不合法")
    private Long skuId;

    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量最少为1")
    private Integer quantity;
}