package cn.hisouten.mall.mq.consumer;

import cn.hisouten.mall.mapper.SeckillProductMapper;
import cn.hisouten.mall.mq.message.OrderCancelMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static cn.hisouten.mall.constant.RedisConstant.MQ_IDEMPOTENT_ORDER_CANCEL;
import static cn.hisouten.mall.constant.RedisConstant.SECKILL_STOCK;
import static cn.hisouten.mall.mq.constant.MqConstant.ORDER_CANCEL_GROUP;
import static cn.hisouten.mall.mq.constant.MqConstant.ORDER_CANCEL_TOPIC;

@Component
@Slf4j
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = ORDER_CANCEL_TOPIC,
        consumerGroup = ORDER_CANCEL_GROUP,
        consumeThreadMax = 10,
        maxReconsumeTimes = 5
)
public class OrderCancelConsumer implements RocketMQListener<OrderCancelMessage> {

    private final SeckillProductMapper seckillProductMapper;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(OrderCancelMessage message) {
        //做幂等标记，防止抖动导致库存重复回滚
        String idempotentKey = MQ_IDEMPOTENT_ORDER_CANCEL + message.getOrderNo();
        Boolean firstTime = stringRedisTemplate.opsForValue()
                .setIfAbsent(idempotentKey, "1", 24, TimeUnit.HOURS);
        if (Boolean.FALSE.equals(firstTime)) {
            log.warn("[订单取消-回滚秒杀] 重复消息，跳过 orderNo={}", message.getOrderNo());
            return;
        }
        for (OrderCancelMessage.Item item : message.getItems()) {
            // 1. 回滚数据库秒杀库存
            seckillProductMapper.restoreSeckillStock(item.getSkuId(), item.getQuantity());
            // 2. 回滚 Redis 秒杀库存
            stringRedisTemplate.opsForValue()
                    .increment(SECKILL_STOCK + item.getSkuId(), item.getQuantity());
        }
    }
}
