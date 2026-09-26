package cn.hisouten.mall.pojo.dto.brand;

import cn.hisouten.mall.pojo.BasePageQuery;
import lombok.Data;

/**
 * 管理员分页查询品牌参数
 */
@Data
public class AdminBrandPageQueryDTO extends BasePageQuery {
    private String keyword;

    private Integer status;

    private Integer deleted;
}
