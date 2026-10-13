package cn.hisouten.mall.mq.consumer;

import cn.hisouten.mall.mapper.SeckillActivityMapper;
import cn.hisouten.mall.mapper.SeckillProductMapper;
import cn.hisouten.mall.mq.message.OrderCancelMessage;
import cn.hisouten.mall.pojo.entity.SeckillActivity;
import cn.hisouten.mall.pojo.entity.SeckillProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.hisouten.mall.constant.RedisConstant.*;
import static cn.hisouten.mall.constant.SeckillConstant.RUNNING;
import static cn.hisouten.mall.mq.constant.MqConstant.ORDER_CANCEL_GROUP;
import static cn.hisouten.mall.mq.constant.MqConstant.ORDER_CANCEL_TOPIC;

@Component
@Slf4j
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = ORDER_CANCEL_TOPIC,
        consumerGroup = ORDER_CANCEL_GROUP,
        consumeThreadMax = 20,
        maxReconsumeTimes = 5
)
public class OrderCancelConsumer implements RocketMQListener<OrderCancelMessage> {

    private final SeckillProductMapper seckillProductMapper;
    private final SeckillActivityMapper seckillActivityMapper;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(OrderCancelMessage message) {
        String idempotentKey = MQ_IDEMPOTENT_ORDER_CANCEL + message.getOrderNo();

        // 1. 幂等校验
        Boolean firstTime = stringRedisTemplate.opsForValue()
                .setIfAbsent(idempotentKey, "1", 24, TimeUnit.HOURS);
        if (Boolean.FALSE.equals(firstTime)) {
            log.warn("[订单取消-回滚秒杀] 重复消息，跳过 orderNo={}", message.getOrderNo());
            return;
        }

        try {
            for (OrderCancelMessage.Item item : message.getItems()) {
                // 2.1 回滚数据库秒杀库存
                seckillProductMapper.restoreSeckillStock(item.getSkuId(), item.getQuantity());

                // 2.2 判断活动状态，只有进行中才回滚 Redis
                SeckillProduct sp = seckillProductMapper.selectBySkuId(item.getSkuId());
                if (sp == null) {
                    log.warn("[订单取消-回滚秒杀] 秒杀商品不存在，跳过Redis回滚 skuId={}", item.getSkuId());
                    continue;
                }
                SeckillActivity activity = seckillActivityMapper.selectById(sp.getActivityId());
                if (activity == null || !Objects.equals(activity.getStatus(), RUNNING)) {
                    // 活动已结束：Redis key 已清理，只回滚 DB
                    log.info("[订单取消-回滚秒杀] 活动已结束，跳过Redis回滚 skuId={}", item.getSkuId());
                    continue;
                }

                // 2.3 回滚 Redis 秒杀库存
                stringRedisTemplate.opsForValue()
                        .increment(SECKILL_STOCK + item.getSkuId(), item.getQuantity());
                // 2.4 回滚 Redis 用户购买记录
                stringRedisTemplate.opsForHash().increment(
                        SECKILL_USERS + item.getSkuId(),
                        message.getUserId().toString(),
                        -item.getQuantity()
                );
            }
        } catch (Exception e) {
            stringRedisTemplate.delete(idempotentKey);
            log.error("[订单取消-回滚秒杀] 处理失败，已删除幂等标记 orderNo={}", message.getOrderNo(), e);
            throw e;
        }
    }
}