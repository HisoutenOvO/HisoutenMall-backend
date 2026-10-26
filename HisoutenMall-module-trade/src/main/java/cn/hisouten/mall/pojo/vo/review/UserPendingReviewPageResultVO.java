package cn.hisouten.mall.pojo.vo.review;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户分页查询待评论列表返回值
 */
@Data
public class UserPendingReviewPageResultVO {

    private Long orderItemId;

    private String orderNo;

    private Long productId;

    private String productName;

    private String productImage;

    private String skuSpecs;

    private LocalDateTime finishTime;

}
