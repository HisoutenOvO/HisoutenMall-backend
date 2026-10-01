package cn.hisouten.mall.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商家查询订单详情返回值
 */
@Data
public class MerchantOrderDetailVO {

    private String orderNo;

    private Integer status;

    private BigDecimal totalAmount;

    private BigDecimal payAmount;

    private String buyerUsername;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private LocalDateTime createTime;

    private LocalDateTime payTime;

    private List<OrderItemVO> items;

}
