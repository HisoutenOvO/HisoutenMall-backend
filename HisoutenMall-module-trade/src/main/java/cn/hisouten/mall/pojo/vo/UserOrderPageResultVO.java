package cn.hisouten.mall.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户分页查询订单返回值
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderPageResultVO {

    private Long id;

    private String orderNo;

    private Long merchantId;

    private String merchantName;

    private BigDecimal payAmount;

    private Integer status;

    private LocalDateTime createTime;

    private List<OrderItemVO> items;
}
