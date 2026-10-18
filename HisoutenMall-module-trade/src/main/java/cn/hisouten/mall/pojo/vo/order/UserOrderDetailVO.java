package cn.hisouten.mall.pojo.vo.order;

import cn.hisouten.mall.pojo.entity.Logistics;
import cn.hisouten.mall.pojo.vo.coupon.OrderCouponVO;
import cn.hisouten.mall.pojo.vo.logistics.UserLogisticsVO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户查询订单详情返回值
 */
@Data
public class UserOrderDetailVO {

    private Long id;

    private String orderNo;

    private Integer status;

    private BigDecimal totalAmount;

    private BigDecimal payAmount;

    private BigDecimal discountAmount;

    private UserLogisticsVO logistics; // 未发货为空

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private LocalDateTime createTime;

    private LocalDateTime payTime;

    private Integer source;

    // 商家信息
    private Long merchantId;

    private String merchantName;

    // 明细
    private List<OrderItemVO> items;

    // 优惠券信息（未用券则为 null）
    private OrderCouponVO couponInfo;
}
