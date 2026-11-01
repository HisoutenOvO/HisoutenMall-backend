package cn.hisouten.mall.mq.consumer;

import cn.hisouten.mall.mq.message.ProductSalesMessage;
import cn.hisouten.mall.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import static cn.hisouten.mall.mq.constant.MqConstant.PRODUCT_ADD_SALES_GROUP;
import static cn.hisouten.mall.mq.constant.MqConstant.PRODUCT_ADD_SALES_TOPIC;

/**
 * 增加ES商品文档销量消费者
 */
@Component
@RequiredArgsConstructor
@Slf4j
@RocketMQMessageListener(
        topic = PRODUCT_ADD_SALES_TOPIC,
        consumerGroup = PRODUCT_ADD_SALES_GROUP
)
public class ProductSalesAddConsumer implements RocketMQListener<ProductSalesMessage> {

    private final ProductSearchService productSearchService;

    @Override
    public void onMessage(ProductSalesMessage message) {
        Long productId = message.getProductId();
        Integer quantity = message.getQuantity();

        log.info("[销量累加] 收到消息, productId: {}, quantity: {}", productId, quantity);

        try {
            // 调用 ES 的原子累加方法
            productSearchService.addSalesInEs(productId, quantity);
        } catch (Exception e) {
            log.error("[销量累加] 失败, productId: {}", productId, e);
            // 抛出异常，让 MQ 重试
            throw e;
        }
    }
}
