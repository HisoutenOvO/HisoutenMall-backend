package cn.hisouten.mall.controller.merchant;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.MerchantSeckillActivityPageQueryDTO;
import cn.hisouten.mall.pojo.dto.MerchantSeckillActivitySaveDTO;
import cn.hisouten.mall.pojo.dto.MerchantSeckillProductAddDTO;
import cn.hisouten.mall.pojo.dto.MerchantSeckillProductUpdateDTO;
import cn.hisouten.mall.pojo.vo.MerchantSeckillActivityDetailVO;
import cn.hisouten.mall.pojo.vo.MerchantSeckillActivityPageResultVO;
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
     * 分页查询秒杀活动
     * @param pageQueryDTO 分页参数
     * @return 返回值
     */
    @GetMapping("/activity/page")
    @Operation(summary = "分页查询秒杀活动")
    public Result<PageResult<MerchantSeckillActivityPageResultVO>> pageQuery(MerchantSeckillActivityPageQueryDTO pageQueryDTO){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家:{} 分页查询秒杀活动",merchantId);
        PageResult<MerchantSeckillActivityPageResultVO> pageResult = seckillService.pageQueryMerchant(merchantId,pageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增秒杀活动
     * @param merchantSeckillActivitySaveDTO 活动参数
     * @return 返回值
     */
    @PostMapping("/activity")
    @Operation(summary = "新增秒杀活动")
    public Result createActivity(@RequestBody MerchantSeckillActivitySaveDTO merchantSeckillActivitySaveDTO){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家：{} 创建秒杀活动",merchantId);
        seckillService.createActivity(merchantId, merchantSeckillActivitySaveDTO);
        return Result.success();
    }

    /**
     * 查询秒杀活动详情
     * @param activityId 活动id
     * @return 返回值
     */
    @GetMapping("/activity/{activityId}")
    @Operation(summary = "查询秒杀活动详情")
    public Result<MerchantSeckillActivityDetailVO> detailQuery(@PathVariable Long activityId){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家:{} 查询秒杀活动:{} 详情",merchantId,activityId);
        MerchantSeckillActivityDetailVO detailVO = seckillService.detailQuery(merchantId,activityId);
        return Result.success(detailVO);
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
        log.info("商家:{} 修改秒杀活动:{}",merchantId,activityId);
        seckillService.updateActivity(merchantId,activityId,merchantSeckillActivitySaveDTO);
        return Result.success();
    }

    /**
     * 删除秒杀活动
     * @param activityId 活动id
     * @return 返回值
     */
    @DeleteMapping("/activity/{activityId}")
    @Operation(summary = "删除秒杀活动")
    public Result delete(@PathVariable Long activityId){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家：{} 删除秒杀活动：{}",merchantId,activityId);
        seckillService.delete(merchantId,activityId);
        return Result.success();
    }

    /**
     * 批量新增秒杀商品
     * @param addDTO 商品参数
     * @return 返回值
     */
    @PostMapping("/product")
    @Operation(summary = "（批量）新增秒杀商品")
    public Result addProduct(@RequestBody MerchantSeckillProductAddDTO addDTO){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家:{} 批量新增秒杀商品",merchantId);
        seckillService.addProduct(merchantId,addDTO);
        return Result.success();
    }

    /**
     * 修改秒杀商品
     * @param productId 商品id
     * @param updateDTO 修改商品参数
     * @return 返回值
     */
    @PutMapping("/product/{productId}")
    @Operation(summary = "修改秒杀商品")
    public Result updateProduct(@PathVariable Long productId,@RequestBody MerchantSeckillProductUpdateDTO updateDTO){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家:{} 修改秒杀商品:{} ",merchantId,productId);
        seckillService.updateProduct(merchantId,productId,updateDTO);
        return Result.success();
    }


    /**
     * 删除秒杀商品
     * @param productId 商品id
     * @return 返回值
     */
    @DeleteMapping("/product/{productId}")
    @Operation(summary = "删除秒杀商品")
    public Result deleteProduct(@PathVariable Long productId){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家：{} 删除秒杀商品:{} ",merchantId,productId);
        seckillService.deleteProduct(merchantId,productId);
        return Result.success();
    }
}
