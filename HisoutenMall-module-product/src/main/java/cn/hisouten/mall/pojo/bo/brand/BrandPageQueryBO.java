package cn.hisouten.mall.pojo.bo.brand;

import cn.hisouten.mall.pojo.BasePageQuery;
import lombok.Data;

/**
 * 用于中转brand分页查询DTO
 */
@Data
public class BrandPageQueryBO extends BasePageQuery {
    private String keyword;

    private Integer status;

    private Integer deleted;
}
