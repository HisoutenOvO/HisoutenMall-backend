package cn.hisouten.mall.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户秒杀商品参数
 */
@Data
public class SeckillExecuteDTO {

    @NotNull(message = "活动ID不能为空")
    private Long activityId;

    @NotNull(message = "商品SKU不能为空")
    private Long skuId;

}