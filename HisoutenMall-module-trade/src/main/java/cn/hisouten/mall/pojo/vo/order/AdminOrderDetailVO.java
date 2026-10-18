package cn.hisouten.mall.pojo.vo.order;

import cn.hisouten.mall.pojo.vo.coupon.OrderCouponVO;
import cn.hisouten.mall.pojo.vo.logistics.UserLogisticsVO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员查询订单详情返回值
 */
@Data
@NoArgsConstructor
public class AdminOrderDetailVO {

    private String orderNo;

    private String merchantName;

    private Integer status;

    private BigDecimal totalAmount;

    private BigDecimal payAmount;

    private BigDecimal discountAmount;

    private UserLogisticsVO logistics; // 未发货为空

    private String buyerUsername;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private LocalDateTime createTime;

    private LocalDateTime payTime;

    private List<OrderItemVO> items;

    private Integer source;

    // 优惠券信息（未用券则为 null）
    private OrderCouponVO couponInfo;
}
