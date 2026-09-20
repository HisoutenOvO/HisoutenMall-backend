package cn.hisouten.mall.pojo.dto.product;

import cn.hisouten.mall.pojo.BasePageQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MerchantProductPageQueryDTO extends BasePageQuery {

    private String keyword;

    private Long categoryId;

    private Integer status;
}
