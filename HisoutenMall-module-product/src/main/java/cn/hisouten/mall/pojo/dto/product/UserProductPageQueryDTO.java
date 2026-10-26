package cn.hisouten.mall.pojo.dto.product;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户分页查询商品参数
 */
@Data
public class UserProductPageQueryDTO extends BasePageQueryDTO {

    @Size(max = 100, message = "搜索关键词过长")
    private String keyword;

    private Long categoryId;

    private Long brandId;

    private Integer sortType;

}