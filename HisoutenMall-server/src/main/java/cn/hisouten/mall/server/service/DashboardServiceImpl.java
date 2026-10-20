package cn.hisouten.mall.server.service;

import cn.hisouten.mall.pojo.vo.order.DailySalesVO;
import cn.hisouten.mall.server.vo.AdminDashboardVO;
import cn.hisouten.mall.server.vo.MerchantDashboardVO;
import cn.hisouten.mall.service.OrderService;
import cn.hisouten.mall.service.ProductService;
import cn.hisouten.mall.service.ReviewService;
import cn.hisouten.mall.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{
    private final OrderService orderService;
    private final ProductService productService;
    private final ReviewService reviewService;
    private final AuthService authService;

    /**
     * 商家获取统计信息
     * @param merchantId 商家id
     * @return 返回值
     */
    @Override
    public MerchantDashboardVO merchantStatsQuery(Long merchantId) {
        BigDecimal todaySales = orderService.getTodaySales(merchantId);
        Long todayOrderCount = orderService.getTodayOrderCount(merchantId);
        BigDecimal totalSales = orderService.getTotalSales(merchantId);
        Long totalOrderCount = orderService.getTodayOrderCount(merchantId);
        Long pendingShipCount = orderService.getPendingShipCount(merchantId);
        Long onSaleProductCount = productService.getOnSaleProductCount(merchantId);
        Long totalReviewCount = reviewService.getTotalReviewCount(merchantId);
        List<DailySalesVO> salesTrend = orderService.getMerchantSalesTrend(merchantId);

        return MerchantDashboardVO.builder().
                todaySales(todaySales)
                .todayOrderCount(todayOrderCount)
                .totalSales(totalSales)
                .totalOrderCount(totalOrderCount)
                .pendingShipCount(pendingShipCount)
                .onSaleProductCount(onSaleProductCount)
                .totalReviewCount(totalReviewCount)
                .salesTrend(salesTrend)
                .build();
    }

    /**
     * 管理员获取统计信息
     * @return 返回值
     */
    @Override
    public AdminDashboardVO adminSTatsQuery() {
        BigDecimal todayPlatformSales = orderService.getTodayPlatformSales();
        Long todayPlatformOrders = orderService.getTodayPlatformOrders();
        Long totalUserCount = authService.getTotalUserCount();
        Long totalMerchantCount = authService.getTotalMerchantCount();
        Long pendingMerchantAudit = authService.getPendingMerchantAudit();
        List<DailySalesVO> salesTrend = orderService.getSalesTrend();

        return AdminDashboardVO.builder()
                .todayPlatformSales(todayPlatformSales)
                .todayPlatformOrders(todayPlatformOrders)
                .totalUserCount(totalUserCount)
                .totalMerchantCount(totalMerchantCount)
                .pendingMerchantAudit(pendingMerchantAudit)
                .salesTrend(salesTrend)
                .build();
    }
}
