package cn.hisouten.mall.mq.consumer;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mq.constant.MqConstant;
import cn.hisouten.mall.mq.dto.OrderTimeoutMessage;
import cn.hisouten.mall.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 超时订单处理消费者类
 */
@Component
@RequiredArgsConstructor
@Slf4j
@RocketMQMessageListener(
        topic = MqConstant.ORDER_TIMEOUT_TOPIC, //主题
        consumerGroup = MqConstant.ORDER_TIMEOUT_GROUP, //消费者组
        consumeThreadMax = 20, //最大线程数
        maxReconsumeTimes = 5  //最大重新消费次数
)
public class OrderTimeoutConsumer implements RocketMQListener<OrderTimeoutMessage> {
    private final OrderService orderService;

    /**
     * 收到消息后立刻调用-订单超时处理
     * @param message 消息体
     */
    @Override
    public void onMessage(OrderTimeoutMessage message){
        log.info("[订单超时-消费] 收到消息 orderNo={}, sendTime={}", message.getOrderNo(), message.getSendTime());
        try{
            orderService.closeTimeoutOrder(message.getOrderNo());
        }catch (BizException e){
            // 业务异常(如订单不存在)，不需要重试，记录日志后返回成功
            log.warn("[订单超时消费] 业务异常，不重试 orderNo={}, msg={}", message.getOrderNo(), e.getMessage());
            return;
        }catch (Exception e){
            // 系统异常(如数据库超时)，抛出触发重试
            log.error("[订单超时消费] 系统异常，将重试 orderNo={}", message.getOrderNo(), e);
            throw e;
        }
    }
}
