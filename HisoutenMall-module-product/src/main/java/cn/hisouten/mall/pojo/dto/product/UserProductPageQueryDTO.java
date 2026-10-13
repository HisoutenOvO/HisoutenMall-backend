package cn.hisouten.mall.pojo.dto.product;

import cn.hisouten.mall.pojo.BasePageQueryDTO;
import lombok.Data;

/**
 * 用户分页查询商品参数
 */
@Data
public class UserProductPageQueryDTO extends BasePageQueryDTO {

    private String keyword;

    private Long categoryId;

    private Long brandId;

}
