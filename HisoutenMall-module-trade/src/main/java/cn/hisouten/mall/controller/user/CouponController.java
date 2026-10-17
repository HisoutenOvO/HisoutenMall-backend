package cn.hisouten.mall.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.AvailableCouponPageQueryDTO;
import cn.hisouten.mall.pojo.vo.AvailableCouponPageResultVO;
import cn.hisouten.mall.pojo.vo.UserUsableCouponVO;
import cn.hisouten.mall.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController("userCouponController")
@RequestMapping("/user/coupon")
@Slf4j
@Tag(name = "用户端——优惠券接口")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    /**
     * 分页查询可灵气优惠券列表
     * @param pageQueryDTO 分页参数
     * @return 返回值
     */
    @GetMapping("/available")
    @Operation(summary = "分页查询可领取优惠券列表")
    public Result<PageResult<AvailableCouponPageResultVO>> availablePageQuery(AvailableCouponPageQueryDTO pageQueryDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 分页查询可领取的优惠券列表",userId);
        PageResult<AvailableCouponPageResultVO> pageResult = couponService.availablePageQuery(userId,pageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 领券
     * @param couponId 优惠券id
     * @return 返回值
     */
    @PostMapping("/{couponId}/receive")
    @Operation(summary = "领券")
    public Result receiveCoupon(@PathVariable Long couponId){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 领取优惠券:{} ",userId,couponId);
        couponService.receiveCoupon(userId,couponId);
        return Result.success();
    }

    /**
     * 查询可用优惠券列表
     * @param merchantId 商家id
     * @param orderAmount 订单金额
     * @return 返回值
     */
    @GetMapping("/usable")
    @Operation(summary = "查询可用优惠券列表")
    public Result<List<UserUsableCouponVO>> usableQuery(@RequestParam Long merchantId, @RequestParam BigDecimal orderAmount){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 查询可用优惠券",userId);
        List<UserUsableCouponVO> usableCouponVOList = couponService.usableQuery(userId,merchantId,orderAmount);
        return Result.success(usableCouponVOList);
    }
}
