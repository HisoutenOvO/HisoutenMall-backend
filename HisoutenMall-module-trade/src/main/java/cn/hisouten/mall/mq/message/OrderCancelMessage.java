package cn.hisouten.mall.mq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 订单取消消息体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String orderNo;
    private Long userId;
    private Integer source;   // 0-普通 1-秒杀
    private List<Item> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item implements Serializable {
        private Long skuId;
        private Integer quantity;
    }
}