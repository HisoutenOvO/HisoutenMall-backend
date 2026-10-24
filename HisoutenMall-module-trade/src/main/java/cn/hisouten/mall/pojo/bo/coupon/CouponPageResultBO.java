package cn.hisouten.mall.pojo.bo.coupon;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用于中转优惠券分页查询返回值的BO类
 */
@Data
public class CouponPageResultBO {

    private Long id;

    private Long merchantId;

    private String merchantName;

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
