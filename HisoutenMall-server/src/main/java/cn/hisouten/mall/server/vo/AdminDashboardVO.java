package cn.hisouten.mall.server.vo;

import cn.hisouten.mall.pojo.vo.order.DailySalesVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 管理端查询统计数据返回值
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDashboardVO {

    // 平台
    private BigDecimal todayPlatformSales;  // 今日平台总GMV

    private Long todayPlatformOrders;       // 今日平台总订单数

    // 用户与商家规模
    private Long totalUserCount;            // 平台总用户数

    private Long totalMerchantCount;        // 平台总商家数

    // 待办指标
    private Long pendingMerchantAudit;      // 待审核商家数

    // 趋势图数据（近7天销售额）
    private List<DailySalesVO> salesTrend;  // 折线图

}