package cn.hisouten.mall.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.AvailableCouponPageQueryDTO;
import cn.hisouten.mall.pojo.vo.AvailableCouponPageResultVO;
import cn.hisouten.mall.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userCouponController")
@RequestMapping("/user/coupon")
@Slf4j
@Tag(name = "用户端——优惠券接口")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @GetMapping("/available")
    @Operation(summary = "分页查询可领取优惠券列表")
    public Result<PageResult<AvailableCouponPageResultVO>> availablePageQuery(AvailableCouponPageQueryDTO pageQueryDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 分页查询可领取的优惠券列表",userId);
        PageResult<AvailableCouponPageResultVO> pageResult = couponService.availablePageQuery(userId,pageQueryDTO);
        return Result.success(pageResult);
    }
}
