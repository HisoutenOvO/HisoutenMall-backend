package cn.hisouten.mall.pojo.dto.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商家新增商品参数
 */
@Data
@NoArgsConstructor
public class MerchantProductAddDTO {

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100, message = "商品名称不能超过100字")
    private String name;

    @Size(max = 200, message = "副标题不能超过200字")
    private String subtitle;

    @NotNull(message = "分类不能为空")
    private Long categoryId;

    private Long brandId;

    @Size(max = 500, message = "主图链接过长")
    private String mainImage;

    @Size(max = 5000, message = "详情不能超过5000字")
    private String detail;

    private Integer status;

    @NotEmpty(message = "商品必须包含至少一个SKU")
    @Valid // 必须加，用于级联校验内部SKU属性
    private List<ProductSkuItemDTO> skuList;
}