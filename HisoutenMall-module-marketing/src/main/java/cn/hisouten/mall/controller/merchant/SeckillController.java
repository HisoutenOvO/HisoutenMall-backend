package cn.hisouten.mall.controller.merchant;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.MerchantSeckillActivitySaveDTO;
import cn.hisouten.mall.service.SeckillOrderService;
import cn.hisouten.mall.service.SeckillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController("merchantSeckillController")
@RequestMapping("/merchant/seckill")
@Slf4j
@Tag(name = "商家端——秒杀接口")
@RequiredArgsConstructor
public class SeckillController {
    private final SeckillOrderService seckillOrderService;
    private final SeckillService seckillService;


    /**
     * 新增秒杀活动
     * @param merchantSeckillActivitySaveDTO 活动参数
     * @return 返回值
     */
    @PostMapping("/activity")
    @Operation(summary = "新增秒杀活动")
    public Result createActivity(@RequestBody MerchantSeckillActivitySaveDTO merchantSeckillActivitySaveDTO){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家：{}创建秒杀活动",merchantId);
        seckillService.createActivity(merchantId, merchantSeckillActivitySaveDTO);
        return Result.success();
    }

    /**
     * 修改秒杀活动
     * @param activityId 活动id
     * @param merchantSeckillActivitySaveDTO 活动参数
     * @return 返回值
     */
    @PutMapping("/activity/{activityId}")
    @Operation(summary = "修改秒杀活动")
    public Result updateActivity(@PathVariable Long activityId,@RequestBody MerchantSeckillActivitySaveDTO merchantSeckillActivitySaveDTO){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家:{}修改秒杀活动:{}",merchantId,activityId);
        seckillService.updateActivity(merchantId,activityId,merchantSeckillActivitySaveDTO);
        return Result.success();
    }
}
