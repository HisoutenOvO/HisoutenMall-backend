package cn.hisouten.mall.controller.merchant;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.MerchantCouponAddDTO;
import cn.hisouten.mall.pojo.dto.MerchantCouponPageQueryDTO;
import cn.hisouten.mall.pojo.dto.MerchantCouponUpdateDTO;
import cn.hisouten.mall.pojo.vo.MerchantCouponDetailVO;
import cn.hisouten.mall.pojo.vo.MerchantCouponPageResultVO;
import cn.hisouten.mall.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController("merchantCouponController")
@RequestMapping("/user/coupon")
@Slf4j
@Tag(name = "商家端——优惠券接口")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;


    /**
     * 新增优惠券
     * @param addDTO 新增参数
     * @return 返回值
     */
    @PostMapping
    @Operation(summary = "新增优惠券")
    public Result addCoupon(@RequestBody MerchantCouponAddDTO addDTO){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家:{} 新增优惠券",merchantId);
        couponService.addCoupon(merchantId,addDTO);
        return Result.success();
    }

    /**
     * 修改优惠券
     * @param updateDTO 修改参数
     * @return 返回值
     */
    @PutMapping("/{couponId}")
    @Operation(summary = "修改优惠券信息")
    public Result updateCoupon(@RequestBody MerchantCouponUpdateDTO updateDTO, @PathVariable Long couponId){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家:{} 修改优惠券:{} 信息",merchantId,couponId);
        couponService.updateCoupon(merchantId,couponId,updateDTO);
        return Result.success();
    }

    /**
     * 分页查询优惠券
     * @param pageQueryDTO 分页条件参数
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询优惠券")
    public Result<PageResult<MerchantCouponPageResultVO>> pageQuery(MerchantCouponPageQueryDTO pageQueryDTO){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家:{} 分页查询优惠券",merchantId);
        PageResult<MerchantCouponPageResultVO> pageResultVO = couponService.merchantPageQuery(merchantId,pageQueryDTO);
        return Result.success(pageResultVO);
    }

    /**
     * 查询优惠券详情
     * @param couponId 优惠券id
     * @return 返回值
     */
    @GetMapping("/{couponId}")
    @Operation(summary = "查询优惠券详情")
    public Result<MerchantCouponDetailVO> detailQuery(@PathVariable Long couponId){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家:{}查询优惠券:{} 详情",merchantId,couponId);
        MerchantCouponDetailVO detailVO = couponService.merchantDetailQuery(merchantId,couponId);
        return Result.success(detailVO);
    }

    /**
     * 删除优惠券
     * @param couponId 优惠券id
     * @return 返回值
     */
    @DeleteMapping("/{couponId}")
    @Operation(summary = "删除优惠券")
    public Result deleted(@PathVariable Long couponId){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家:{} 删除优惠券:{} ",merchantId,couponId);
        couponService.deleteCoupon(merchantId,couponId);
        return Result.success();
    }
}
