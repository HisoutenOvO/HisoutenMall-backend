package cn.hisouten.mall.pojo.dto;

import cn.hisouten.mall.pojo.BasePageQueryDTO;
import lombok.Data;

/**
 * 通用的分页查询商品评价参数
 */
@Data
public class ReviewPageQueryDTO extends BasePageQueryDTO {
    private Long productId;
    private Integer rating;
}
