package cn.hisouten.mall.task;

import cn.hisouten.mall.dao.mapper.ProductMapper;
import cn.hisouten.mall.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 同步ES商品销量定时任务类
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductSalesSyncTask {

    private final ProductMapper productMapper;
    private final ProductSearchService productSearchService;

    /**
     * 每分钟同步一次销量到 ES
     * cron 表达式：0 0/5 * * * ?
     */
    @Scheduled(cron = "0 * * * * ?")
    public void syncSalesToEs() {
        log.info("[定时任务] 开始同步商品销量到 ES...");
        long start = System.currentTimeMillis();

        try {
            // 1. 查出所有有销量的商品及总销量
            List<Map<String, Object>> salesList = productMapper.selectTotalSalesByProductId();

            if (salesList == null || salesList.isEmpty()) {
                return;
            }

            // 2. 循环更新 ES
            for (Map<String, Object> map : salesList) {
                Long productId = ((Number) map.get("productId")).longValue();
                Integer totalSales = ((Number) map.get("totalSales")).intValue();

                productSearchService.updateSalesInEs(productId, totalSales);
            }
            log.info("[定时任务] 同步完成，共处理 {} 个商品，耗时 {} ms", salesList.size(), System.currentTimeMillis() - start);

        } catch (Exception e) {
            log.error("[定时任务] 同步销量异常", e);
        }
    }
}