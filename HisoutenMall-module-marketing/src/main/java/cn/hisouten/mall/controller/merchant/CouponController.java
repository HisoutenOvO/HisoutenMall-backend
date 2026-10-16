package cn.hisouten.mall.controller.merchant;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.MerchantCouponAddDTO;
import cn.hisouten.mall.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
