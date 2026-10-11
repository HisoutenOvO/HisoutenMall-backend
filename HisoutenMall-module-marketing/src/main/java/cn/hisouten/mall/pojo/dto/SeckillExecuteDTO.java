package cn.hisouten.mall.pojo.dto;

import lombok.Data;

/**
 * 用户秒杀商品参数
 */
@Data
public class SeckillExecuteDTO {

    private Long activityId;

    private Long skuId;

}
