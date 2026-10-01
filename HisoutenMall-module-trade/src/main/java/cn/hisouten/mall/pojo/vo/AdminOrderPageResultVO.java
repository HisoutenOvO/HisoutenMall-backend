package cn.hisouten.mall.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员分页查询订单返回值
 */
@Data
public class AdminOrderPageResultVO {
    private Long id;

    private String orderNo;

    private String merchantName;

    private String buyerName;

    private BigDecimal payAmount;

    private Integer status;

    private LocalDateTime createTime;

    private List<OrderItemVO> items;
}
