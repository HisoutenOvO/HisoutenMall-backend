package cn.hisouten.mall.task;

import cn.hisouten.mall.mapper.OrderItemMapper;
import cn.hisouten.mall.mapper.OrderMapper;
import cn.hisouten.mall.pojo.entity.Order;
import cn.hisouten.mall.pojo.entity.OrderItem;
import cn.hisouten.mall.service.OrderService;
import cn.hisouten.mall.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static cn.hisouten.mall.constant.StatusConstant.CANCELLED;

/**
 * 超时订单处理任务
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderTimeoutTask {
    private final OrderMapper orderMapper;
    private final OrderService orderService;

    @Scheduled(cron = "0 * * * * ?")
    public void closeTimeOutOrders() {
        log.info("扫描超时订单中……");
        LocalDateTime deadLineTime = LocalDateTime.now().minusMinutes(30);
        List<Order> orderList = orderMapper.selectByCreateTime(deadLineTime);
        for (Order order : orderList) {
            try {
                orderService.closeTimeoutOrder(order.getOrderNo());
            } catch (Exception e) {
                log.error("兜底关单失败 orderNo={}", order.getOrderNo(), e);
            }
        }
    }
}