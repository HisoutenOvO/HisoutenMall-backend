package cn.hisouten.mall.pojo.dto.category;

import cn.hisouten.mall.pojo.BasePageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用的分页查询分类参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPageQueryDTO extends BasePageQuery {

    private String keyword;

}
