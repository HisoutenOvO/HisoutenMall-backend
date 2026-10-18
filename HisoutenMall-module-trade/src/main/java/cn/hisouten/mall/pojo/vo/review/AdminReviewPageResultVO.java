package cn.hisouten.mall.pojo.vo.review;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理端分页查询评价列表
 */
@Data
@NoArgsConstructor
public class AdminReviewPageResultVO {

    private Long id;

    private String productName;

    private String merchantName;

    private String nickname;     // 完全的用户昵称

    private String avatar;

    private Integer rating;

    private String content;

    private List<String> images; // 解析逗号分隔的 URL

    private String skuSpecs;

    private LocalDateTime createTime;

}
