package cn.hisouten.mall.mq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 超时订单处理消息体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTimeoutMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // 订单号
    private String orderNo;

    // 消息发送时间戳(用于排查问题)
    private Long sendTime;
}