package cn.hisouten.mall.constant;

/**
 * 秒杀活动相关常量
 */
public class SeckillConstant {

    //秒杀活动常量

    //未开始
    public static final int PENDING = 0;

    //进行中
    public static final int RUNNING = 1;

    //已结束
    public static final int COMPLETED = 2;

    //下单成功
    public static final String ORDER_COMPLETED = "抢单成功！正在排队处理中，请稍等结果";

    //默认活动名称
    public static final String DEFAULT_ACTIVITY_NAME = "的限时秒杀活动！";

    //订单来源——秒杀
    public static final int SECKILL = 1;

    //订单来源——普通
    public static final int NORMAL = 0;
}
