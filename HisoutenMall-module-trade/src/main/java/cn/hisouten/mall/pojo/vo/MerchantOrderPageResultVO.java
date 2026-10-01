package cn.hisouten.mall.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商家端分页查询订单列表
 */
@Data
public class MerchantOrderPageResultVO {

    private Long id;

    private String orderNo;

    private String buyerName;

    private BigDecimal payAmount;

    private Integer status;

    private LocalDateTime createTime;

    private List<OrderItemVO> items;

}
