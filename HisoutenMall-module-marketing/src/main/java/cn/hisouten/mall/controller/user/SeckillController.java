package cn.hisouten.mall.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.SeckillExecuteDTO;
import cn.hisouten.mall.service.SeckillOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userSeckillController")
@RequestMapping("/user/seckill")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "用户端——秒杀接口")
public class SeckillController {
    private final SeckillOrderService seckillOrderService;

    /**
     * 用户秒杀商品
     * @param seckillExecuteDTO 商品参数
     * @return 返回下单成功
     */
    @PostMapping("/execute")
    @Operation(summary = "秒杀商品")
    public Result<String> execute(@RequestBody SeckillExecuteDTO seckillExecuteDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{}秒杀商品{}",userId,seckillExecuteDTO.getSkuId());
        String success = seckillOrderService.seckillProduct(userId,seckillExecuteDTO);
        return Result.success(success);
    }
}
