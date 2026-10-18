package cn.hisouten.mall.pojo.vo.review;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 三端通用的查询评价详情返回值
 */
@Data
@NoArgsConstructor
public class ReviewDetailVO {

    private Long id;

    private String merchantName; // 给管理端看

    // 评价人信息（脱敏后）
    private String nickname;

    private String avatar;

    private Integer rating;

    private String content;        // 完整长文本

    private List<String> images;   // 完整图片列表

    private LocalDateTime createTime;

    // 购买信息快照
    private String productName;

    private String productImage;

    private String skuSpecs;
    // 订单号
    private String orderNo;
}
