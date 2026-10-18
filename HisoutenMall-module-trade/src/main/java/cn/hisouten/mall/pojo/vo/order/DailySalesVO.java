package cn.hisouten.mall.pojo.vo.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 查询每日营业额返回值
 */
@Data
public class DailySalesVO {

    private String date;       // "10-15"

    private BigDecimal amount; // 当日销售额

}
