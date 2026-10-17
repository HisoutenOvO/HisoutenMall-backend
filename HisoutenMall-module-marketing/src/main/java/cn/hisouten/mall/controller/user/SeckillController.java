package cn.hisouten.mall.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.annotation.Log;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.SeckillExecuteDTO;
import cn.hisouten.mall.pojo.vo.activity.UserSeckillActivityListVO;
import cn.hisouten.mall.pojo.vo.activity.UserSeckillActivityDetailVO;
import cn.hisouten.mall.service.SeckillOrderService;
import cn.hisouten.mall.service.SeckillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userSeckillController")
@RequestMapping("/user/seckill")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "用户端——秒杀接口")
public class SeckillController {
    private final SeckillOrderService seckillOrderService;
    private final SeckillService seckillService;

    /**
     * 用户秒杀商品
     * @param seckillExecuteDTO 商品参数
     * @return 返回下单成功
     */
    @PostMapping("/execute")
    @Log("用户秒杀下单")
    @Operation(summary = "秒杀商品")
    public Result<String> execute(@RequestBody SeckillExecuteDTO seckillExecuteDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{}秒杀商品{}",userId,seckillExecuteDTO.getSkuId());
        String success = seckillOrderService.seckillProduct(userId,seckillExecuteDTO);
        return Result.success(success);
    }

    /**
     * 列表查询秒杀活动
     * @return 返回值
     */
    @GetMapping("/activity")
    @Operation(summary = "列表查询秒杀活动")
    public Result<List<UserSeckillActivityListVO>> listQuery(){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 列表查询秒杀活动",userId);
        List<UserSeckillActivityListVO> voList = seckillService.listQuery();
        return Result.success(voList);
    }

    /**
     * 查询秒杀活动详情
     * @param activityId 活动id
     * @return 返回值
     */
    @GetMapping("/activity/{activityId}")
    @Operation(summary = "查询秒杀活动详情")
    public Result<UserSeckillActivityDetailVO> detailQuery(@PathVariable Long activityId){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 查询秒杀活动详情:{}",userId,activityId);
        UserSeckillActivityDetailVO detailVO = seckillService.userDetailQuery(activityId);
        return Result.success(detailVO);
    }
}
