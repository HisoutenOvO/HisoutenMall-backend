package cn.hisouten.mall.pojo.dto.user.category;

import cn.hisouten.mall.pojo.BasePageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPageQueryDTO extends BasePageQuery {

    private String keyword;

}
