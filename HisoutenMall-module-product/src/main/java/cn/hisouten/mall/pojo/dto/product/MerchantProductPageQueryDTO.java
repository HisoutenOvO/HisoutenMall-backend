package cn.hisouten.mall.pojo.dto.product;

import cn.hisouten.mall.pojo.BasePageQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商家分页查询商品参数
 */
@Data
@NoArgsConstructor
public class MerchantProductPageQueryDTO extends BasePageQuery {

    private String keyword;

    private Long categoryId;

    private Long brandId;

    private Integer status;
}
