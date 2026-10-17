package cn.hisouten.mall.pojo.vo.review;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 商家查询商品评价统计返回值
 */
@Data
@NoArgsConstructor
public class MerchantReviewStatisticsVO {

    private Integer totalCount;      // 总评价数

    private BigDecimal avgRating;    // 平均分

    private Integer goodCount;       // 好评数（≥4星）

    private Integer goodRate;        // 好评率（百分比整数）

    private Map<Integer, Integer> ratingCountMap;  // 各星级数量

}
