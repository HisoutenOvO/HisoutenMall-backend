package cn.hisouten.mall.pojo.dto.review;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import lombok.Data;

/**
 * 管理员分页查询评价参数
 */
@Data
public class AdminReviewPageQueryDTO extends BasePageQueryDTO {

    private Long productId;

    private Integer rating;

    private Long merchantId;

    private String merchantName; //模糊查询

    private String keyword;

}
