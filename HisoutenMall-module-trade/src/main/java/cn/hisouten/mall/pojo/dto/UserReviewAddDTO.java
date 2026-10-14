package cn.hisouten.mall.pojo.dto;

import lombok.Data;

/**
 * 用户新增评价参数
 */
@Data
public class UserReviewAddDTO {

    private Long orderItemId;

    private Integer rating;

    private String content;

    private String images;

}
