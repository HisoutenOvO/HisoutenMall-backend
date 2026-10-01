package cn.hisouten.mall.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户创建订单返回值
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderCreateVO {
    private List<String> orderNos;      // 拆单后的多个订单号

    private BigDecimal totalAmount;     // 合计金额
}
