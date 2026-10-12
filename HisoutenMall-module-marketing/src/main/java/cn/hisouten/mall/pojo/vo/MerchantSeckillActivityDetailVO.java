package cn.hisouten.mall.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商家端查询秒杀活动详情返回值
 */
@Data
@NoArgsConstructor
public class MerchantSeckillActivityDetailVO {

    private Long id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;

    private List<MerchantSeckillProductVO> products;   // 含秒杀商品列表

}
