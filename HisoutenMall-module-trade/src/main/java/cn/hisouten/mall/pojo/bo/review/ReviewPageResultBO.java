package cn.hisouten.mall.pojo.bo.review;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用于中转评价分页查询的BO类
 */
@Data
public class ReviewPageResultBO {

    private Long id;

    private String nickname;     // 脱敏的用户昵称

    private String avatar;

    private Integer rating;

    private String content;

    private String images; // 解析逗号分隔的 URL

    private String skuSpecs;

    private LocalDateTime createTime;

}
