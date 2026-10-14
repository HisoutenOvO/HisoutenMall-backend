package cn.hisouten.mall.pojo.dto;

import cn.hisouten.mall.pojo.BasePageQueryDTO;
import lombok.Data;

/**
 * 用户分页查询商品评价参数
 */
@Data
public class UserReviewPageQueryDTO extends BasePageQueryDTO {
    private Long productId;
    private Integer rating;
}
