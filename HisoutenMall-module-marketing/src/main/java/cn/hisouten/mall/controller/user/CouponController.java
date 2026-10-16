package cn.hisouten.mall.controller.user;

import cn.hisouten.mall.service.CouponService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userCouponController")
@RequestMapping("/user/coupon")
@Slf4j
@Tag(name = "用户端——优惠券接口")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;
}
