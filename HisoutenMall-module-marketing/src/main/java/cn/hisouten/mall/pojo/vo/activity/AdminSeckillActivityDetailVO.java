package cn.hisouten.mall.pojo.vo.activity;

import cn.hisouten.mall.pojo.vo.product.MerchantSeckillProductVO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员查询秒杀活动详情返回值
 */
@Data
@NoArgsConstructor
public class AdminSeckillActivityDetailVO {

    private Long id;

    private String merchantName;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;

    private List<MerchantSeckillProductVO> products;   // 含秒杀商品列表

}
