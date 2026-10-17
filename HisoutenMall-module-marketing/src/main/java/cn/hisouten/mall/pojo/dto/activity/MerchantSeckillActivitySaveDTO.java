package cn.hisouten.mall.pojo.dto.activity;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商家端新增或修改秒杀活动参数
 */
@Data
public class MerchantSeckillActivitySaveDTO {

    @Size(max = 100, message = "活动名称不能超过100字")
    private String name;

    @NotNull(message = "开始时间不能为空")
    @Future(message = "开始时间必须在未来")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

}