package cn.hisouten.mall.mq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 秒杀订单消息体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillOrderMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long activityId;

    private Long skuId;

    //时间戳形式记录发送时间
    private Long sendTime;
}