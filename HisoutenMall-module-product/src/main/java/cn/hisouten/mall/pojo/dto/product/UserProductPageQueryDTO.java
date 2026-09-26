package cn.hisouten.mall.pojo.dto.product;

import cn.hisouten.mall.pojo.BasePageQuery;
import lombok.Data;

/**
 * 用户分页查询商品参数
 */
@Data
public class UserProductPageQueryDTO extends BasePageQuery {

    private String keyword;

    private Long categoryId;

    private Long brandId;

}
