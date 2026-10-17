package cn.hisouten.mall.pojo.dto.product;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商家分页查询商品参数
 */
@Data
@NoArgsConstructor
public class MerchantProductPageQueryDTO extends BasePageQueryDTO {

    @Size(max = 100, message = "搜索关键词过长")
    private String keyword;

    private Long categoryId;

    private Long brandId;

    private Integer status;

    private Integer deleted;
}