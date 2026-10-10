package cn.hisouten.mall.mq.constant;

/**
 * mq常量类
 */
public class MqConstant {

    // ========== 订单超时关闭 ==========
    public static final String ORDER_TIMEOUT_TOPIC = "order-timeout-topic";
    public static final String ORDER_TIMEOUT_GROUP = "order-timeout-consumer-group";

    // ========== 秒杀异步下单 ==========
    public static final String SECKILL_ORDER_TOPIC = "seckill-order-topic";
    public static final String SECKILL_ORDER_GROUP = "seckill-order-consumer-group";

    // ========== 延迟等级 从1秒到2小时 ==========
    public static final int ONE_SECOND_DELAY = 1;

    public static final int FIVE_SECOND_DELAY = 2;

    public static final int TEN_SECOND_DELAY = 3;

    public static final int THIRTY_SECOND_DELAY = 4;

    public static final int ONE_MINUTE_DELAY = 5;

    public static final int TWO_MINUTE_DELAY = 6;

    public static final int THREE_MINUTE_DELAY = 7;

    public static final int FOUR_MINUTE_DELAY = 8;

    public static final int FIVE_MINUTE_DELAY = 9;

    public static final int SIX_MINUTE_DELAY = 10;

    public static final int SEVEN_MINUTE_DELAY = 11;

    public static final int EIGHT_MINUTE_DELAY = 12;

    public static final int NINE_MINUTE_DELAY = 13;

    public static final int TEN_MINUTE_DELAY = 14;

    public static final int TWENTY_MINUTE_DELAY = 15;

    public static final int THIRTY_MINUTE_DELAY = 16;

    public static final int ONE_HOUR_DELAY = 17;

    public static final int TWO_HOUR_DELAY = 18;

}