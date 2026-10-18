package cn.hisouten.mall.constant;

/**
 * 系统状态常量类
 */
public class StatusConstant {

    // 启用/禁用状态
    public static final int ENABLED = 1; //启用

    public static final int DISABLED = 0; //禁用

    // 订单支付状态
    public static final int PENDING_PAYMENT = 0; //待支付

    public static final int PAID = 1; //已支付

    public static final int CANCELLED = 2; //已取消

    public static final int ORDER_SHIPPED = 3; //已发货

    public static final int COMPLETED = 4; //已完成

    public static final int REFUND = 5; //已退款

    //物流状态
    public static final int PENDING_SHIP = 0; //待发货

    public static final int LOGISTICS_SHIPPED = 1; //已发货

    public static final int SIGNED = 2; //已签收

    //优惠券状态
    public static final int NOT_START = 0; //未开始

    public static final int IN_PROGRESS = 1; //进行中

    public static final int FINISHED = 2; //已结束

    public static final int NOT_USED = 0; //未使用

    public static final int USED = 1; //已使用

    public static final int EXPIRED = 2; //已过期


    //审核状态
    public static final int PENDING_AUDIT = 0; //待审核

    public static final int PASSED = 1; //通过

    public static final int REJECT = 2; //驳回

}
