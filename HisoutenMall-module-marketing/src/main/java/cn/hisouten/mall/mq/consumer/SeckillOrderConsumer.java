package cn.hisouten.mall.mq.consumer;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mq.dto.SeckillOrderMessage;
import cn.hisouten.mall.service.SeckillOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import static cn.hisouten.mall.mq.constant.MqConstant.SECKILL_ORDER_GROUP;
import static cn.hisouten.mall.mq.constant.MqConstant.SECKILL_ORDER_TOPIC;

/**
 * 秒杀订单消费者
 */
@Component
@Slf4j
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = SECKILL_ORDER_TOPIC,
        consumerGroup = SECKILL_ORDER_GROUP,
        consumeThreadMax = 20,
        maxReconsumeTimes = 5
)
public class SeckillOrderConsumer implements RocketMQListener<SeckillOrderMessage> {

    private final SeckillOrderService seckillOrderService;

    /**
     * 收到消息立刻调用
     * @param message 消息体
     */
    @Override
    public void onMessage(SeckillOrderMessage message){
        log.info("[秒杀订单-消费] 收到消息 userId={},activityId={},skuId={},sendTime={}",
                message.getUserId(), message.getActivityId(),message.getSkuId(),message.getSendTime());
        try{
            seckillOrderService.createSeckillOrder(message);
        }catch (BizException e){
            //捕捉业务异常，不重试
            log.error("[秒杀订单-消费] 秒杀业务异常，不重试： userId={},activityId={},skuId={},sendTime={}",
                    message.getUserId(), message.getActivityId(),message.getSkuId(),message.getSendTime());
        }catch(Exception e){
            log.error("[秒杀订单-消费] 秒杀系统异常，重试： userId={},activityId={},skuId={},sendTime={}",
                    message.getUserId(), message.getActivityId(),message.getSkuId(),message.getSendTime());
            throw e;
        }
    }
}
