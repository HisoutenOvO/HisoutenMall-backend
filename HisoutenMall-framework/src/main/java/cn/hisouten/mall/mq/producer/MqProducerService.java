package cn.hisouten.mall.mq.producer;

import cn.hisouten.mall.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static cn.hisouten.mall.mq.constant.MQExceptionConstant.DELAY_MESSAGE_SEND_FAILED;
import static cn.hisouten.mall.mq.constant.MQExceptionConstant.MESSAGE_SEND_FAILED;

/**
 * 自封装的mq生产者service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MqProducerService {

    private final RocketMQTemplate rocketMQTemplate;

    /**
     * 发送消息
     * @param topic 消息主题
     * @param payload 消息内容
     */
    public void send(String topic,Object payload){
        try{
            SendResult result = rocketMQTemplate.syncSend(
                    topic,
                    MessageBuilder.withPayload(payload).build()
            );
            log.info("[MQ发送] topic={}, payload={}, msgId={}, status={}",topic,payload,result.getMsgId(),result.getSendStatus());
        }catch (Exception e){
            log.error("[MQ发送失败] topic={}, payload={}", topic, payload, e);
            throw new BizException(MESSAGE_SEND_FAILED);
        }
    }

    /**
     * 发送延迟消息
     * @param topic 主题
     * @param payload 内容
     * @param delayLevel 延迟等级，1-18级，16级是30分钟
     */
    public void sendDelay(String topic,Object payload,int delayLevel){
        try{
            SendResult result = rocketMQTemplate.syncSend(
                    topic,
                    MessageBuilder.withPayload(payload).build(),
                    3000,
                    delayLevel
            );
            log.info("[MQ延时发送] topic={}, delayLevel={}, payload={}, msgId={}", topic, delayLevel, payload, result.getMsgId());
        }catch (Exception e){
            log.error("[MQ延时发送失败] topic={}, delayLevel={}, payload={}", topic, delayLevel, payload, e);
            throw new BizException(DELAY_MESSAGE_SEND_FAILED);
        }
    }


}
