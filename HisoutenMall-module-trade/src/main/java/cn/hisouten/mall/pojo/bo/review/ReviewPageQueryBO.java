package cn.hisouten.mall.pojo.bo.review;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import lombok.Data;

/**
 * 用于中转分页查询评价条件的BO类
 */
@Data
public class ReviewPageQueryBO extends BasePageQueryDTO {

    private Long productId;

    private Integer rating;

    private Long merchantId;

    private Long userId;

    private String merchantName; //模糊查询

    private String content;
}
