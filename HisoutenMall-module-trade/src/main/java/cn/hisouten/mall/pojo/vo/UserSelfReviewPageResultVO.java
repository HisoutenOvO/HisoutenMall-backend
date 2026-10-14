package cn.hisouten.mall.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户分页查询自己评价列表参数
 */
@Data
@NoArgsConstructor
public class UserSelfReviewPageResultVO {

    private Long id;

    private Integer rating;

    private String content;

    private List<String> images; // 解析逗号分隔的 URL

    private String skuSpecs;

    private LocalDateTime createTime;

}
