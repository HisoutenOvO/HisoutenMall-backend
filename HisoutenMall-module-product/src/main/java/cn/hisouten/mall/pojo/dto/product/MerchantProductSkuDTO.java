package cn.hisouten.mall.pojo.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商家修改sku参数
 */
@Data
public class MerchantProductSkuDTO {

    @Size(max = 255, message = "规格描述不能超过255字")
    private String specs;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer stock;

    @Size(max = 500, message = "图片链接过长")
    private String image;

}