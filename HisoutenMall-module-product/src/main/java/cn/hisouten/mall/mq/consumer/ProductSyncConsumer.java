package cn.hisouten.mall.mq.consumer;

import cn.hisouten.mall.mq.constant.MqConstant;
import cn.hisouten.mall.mq.message.ProductSyncMessage;
import cn.hisouten.mall.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@RocketMQMessageListener(
        topic = MqConstant.PRODUCT_SYNC_TOPIC,
        consumerGroup = MqConstant.PRODUCT_SYNC_GROUP
)
public class ProductSyncConsumer implements RocketMQListener<ProductSyncMessage> {

    private final ProductSearchService productSearchService;

    @Override
    public void onMessage(ProductSyncMessage message) {
        log.info("[商品同步] 收到消息: {}", message);
        try {
            if ("DELETE".equals(message.getType())) {
                // 删除 ES 中的文档
                productSearchService.deleteById(message.getProductId());
            } else {
                // SAVE 或 SALES 操作，都直接全量覆盖保存
                productSearchService.syncToEs(message.getProductId());
            }
        } catch (Exception e) {
            log.error("[商品同步] 同步异常, productId: {}", message.getProductId(), e);
            // 抛出异常让 MQ 重试
            throw e;
        }
    }
}
