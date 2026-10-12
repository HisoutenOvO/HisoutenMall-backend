package cn.hisouten.mall.controller.merchant;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.MerchantSeckillActivityAddDTO;
import cn.hisouten.mall.service.SeckillOrderService;
import cn.hisouten.mall.service.SeckillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param merchantSeckillActivityAddDTO 活动参数
     * @return 返回值
     */
    @PostMapping("/activity")
    @Operation(summary = "新增秒杀活动")
    public Result createActivity(@RequestBody MerchantSeckillActivityAddDTO merchantSeckillActivityAddDTO){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家：{}创建秒杀活动",merchantId);
        seckillService.createActivity(merchantId,merchantSeckillActivityAddDTO);
        return Result.success();
    }
}
