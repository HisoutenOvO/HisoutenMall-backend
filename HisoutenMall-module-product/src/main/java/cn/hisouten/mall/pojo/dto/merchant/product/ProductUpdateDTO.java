package cn.hisouten.mall.pojo.dto.merchant.product;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductUpdateDTO {

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100, message = "商品名称不能超过100字")
    private String name;

    @Size(max = 200, message = "副标题不能超过200字")
    private String subtitle;

    @NotNull(message = "分类不能为空")
    private Long categoryId;

    private Long brandId;

    private String mainImage;

    @Size(max = 5000, message = "详情不能超过5000字")
    private String detail;

}