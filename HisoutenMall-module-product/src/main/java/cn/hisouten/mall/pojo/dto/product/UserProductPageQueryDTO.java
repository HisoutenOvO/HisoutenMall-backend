package cn.hisouten.mall.pojo.dto.product;

import cn.hisouten.mall.pojo.BasePageQuery;
import lombok.Data;

@Data
public class UserProductPageQueryDTO extends BasePageQuery {

    private String keyword;

    private Long categoryId;

}
