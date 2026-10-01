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

    public static final int CANCELED = 2; //已取消

    public static final int COMPLETED = 3; //已完成

    public static final int REFUND = 4; //已退款

}
