package cn.hisouten.mall.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商家端新增秒杀活动参数
 */
@Data
public class MerchantSeckillActivityAddDTO {

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
