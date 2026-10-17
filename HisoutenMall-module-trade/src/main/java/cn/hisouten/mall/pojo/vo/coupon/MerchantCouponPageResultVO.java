package cn.hisouten.mall.pojo.vo.coupon;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商家端分页查询优惠券返回值
 */
@Data
@NoArgsConstructor
public class MerchantCouponPageResultVO {

    private Long id;

    private String name;

    private Integer type;

    private BigDecimal value;

    private BigDecimal minAmount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer total;

    private Integer remain;

    private Integer receivedCount;   // 已领取数量

    private Integer usedCount;       // 已使用数量（统计）

    private Integer status;          // 0-未开始 1-进行中 2-已结束

    private LocalDateTime createTime;

}