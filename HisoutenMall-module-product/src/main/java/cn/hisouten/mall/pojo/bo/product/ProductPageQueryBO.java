package cn.hisouten.mall.pojo.bo.product;

import cn.hisouten.mall.pojo.BasePageQuery;
import lombok.Data;

/**
 * 用于中转product分页查询DTO
 */
@Data
public class ProductPageQueryBO extends BasePageQuery {

    private String keyword;

    private Long categoryId;

    private Long brandId;

    private Integer status;

    private Long merchantId;

    private Integer deleted;
}
