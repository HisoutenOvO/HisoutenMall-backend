package cn.hisouten.mall.pojo.dto.common;

import cn.hisouten.mall.pojo.BasePageQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductPageQueryDTO extends BasePageQuery {

    private String keyword;

    private Long categoryId;
}
