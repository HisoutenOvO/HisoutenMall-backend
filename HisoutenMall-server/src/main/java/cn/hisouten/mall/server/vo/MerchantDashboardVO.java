package cn.hisouten.mall.server.vo;

import cn.hisouten.mall.pojo.vo.order.DailySalesVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商家端查询统计数据返回值
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantDashboardVO {
    // 核心指标
    private BigDecimal todaySales;      // 今日销售额

    private Long todayOrderCount;       // 今日订单数

    private BigDecimal totalSales;      // 累计销售额

    private Long totalOrderCount;       // 累计订单数

    // 待办指标
    private Long pendingShipCount;      // 待发货订单数 (status = 1-已支付)

    // 商品与数据指标
    private Long onSaleProductCount;    // 在售商品数

    private Long totalReviewCount;      // 累计评价数

    private List<DailySalesVO> salesTrend; // 近 7 天销售趋势

}
