package cn.hisouten.mall.task;

import cn.hisouten.mall.mapper.OrderItemMapper;
import cn.hisouten.mall.mapper.OrderMapper;
import cn.hisouten.mall.pojo.entity.Order;
import cn.hisouten.mall.pojo.entity.OrderItem;
import cn.hisouten.mall.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static cn.hisouten.mall.constant.StatusConstant.CANCELLED;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderTimeoutTask {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    private final ProductService productService;


    /**
     * 每分钟扫描一次超时未支付订单，30分钟超时
     */
    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void closeTimeOutOrders(){
        log.info("扫描超时订单中……");
        //截止时间
        LocalDateTime deadLineTime = LocalDateTime.now().minusMinutes(30);
        List<Order> orderList = orderMapper.selectByCreateTime(deadLineTime);
        for (Order order : orderList) {
            order.setStatus(CANCELLED);
            order.setCancelTime(LocalDateTime.now());
            orderMapper.updateById(order);
            List<OrderItem> itemList = orderItemMapper.selectByOrderId(order.getId());
            for (OrderItem item : itemList) {
                productService.restoreStock(item.getSkuId(),item.getQuantity());
            }
            log.info("订单超时关闭：{}", order.getOrderNo());
        }
    }
}
