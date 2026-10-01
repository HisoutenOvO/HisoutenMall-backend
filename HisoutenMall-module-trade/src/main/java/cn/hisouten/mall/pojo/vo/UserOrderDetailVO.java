package cn.hisouten.mall.pojo.vo;

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

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private LocalDateTime createTime;

    private LocalDateTime payTime;

    // 商家信息
    private Long merchantId;

    private String merchantName;

    // 明细
    private List<OrderItemVO> items;
}
