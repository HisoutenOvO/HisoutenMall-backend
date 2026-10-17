package cn.hisouten.mall.pojo.vo.activity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 商家端分页查询秒杀活动返回值
 */
@Data
@NoArgsConstructor
public class MerchantSeckillActivityPageResultVO {

    private Long id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;

    private Integer productCount;   // 该活动下秒杀商品数量

    private LocalDateTime createTime;

}
