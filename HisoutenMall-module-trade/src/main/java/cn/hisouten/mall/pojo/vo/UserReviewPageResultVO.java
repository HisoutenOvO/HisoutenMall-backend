package cn.hisouten.mall.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户端分页查询评价返回值
 */
@Data
public class UserReviewPageResultVO {

    private Long id;

    private String nickname;

    private String avatar;

    private Integer rating;

    private String content;

    private List<String> images; // 解析逗号分隔的 URL

    private String skuSpecs;

    private LocalDateTime createTime;

}
