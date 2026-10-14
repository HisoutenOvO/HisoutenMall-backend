package cn.hisouten.mall.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商家端分页查询评价返回值
 */
@Data
@NoArgsConstructor
public class MerchantReviewPageResultVO {
    private Long id;

    private String nickname;     // 脱敏的用户昵称

    private String avatar;

    private Integer rating;

    private String content;

    private List<String> images; // 解析逗号分隔的 URL

    private String skuSpecs;

    private LocalDateTime createTime;
}
