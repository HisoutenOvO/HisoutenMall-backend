package cn.hisouten.mall.pojo.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品SKU主要项参数，用于作其他类成员变量
 */
@Data
@NoArgsConstructor
public class ProductSkuItemDTO {

    // 修改商品时，如果是新增的SKU，skuId为空；如果是修改，则不为空。所以这里不加 @NotNull
    private Long skuId;

    @Size(max = 255, message = "规格描述不能超过255字")
    private String specs;

    @NotNull(message = "SKU价格不能为空")
    @DecimalMin(value = "0.01", message = "SKU价格必须大于0")
    private BigDecimal price;

    @NotNull(message = "SKU库存不能为空")
    @Min(value = 0, message = "SKU库存不能为负数")
    private Integer stock;

    @Size(max = 500, message = "SKU图片链接过长")
    private String image;
}