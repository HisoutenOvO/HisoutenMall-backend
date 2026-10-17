package cn.hisouten.mall.pojo.dto.activity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商家端新增或修改秒杀活动参数
 */
@Data
public class MerchantSeckillActivitySaveDTO {

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
