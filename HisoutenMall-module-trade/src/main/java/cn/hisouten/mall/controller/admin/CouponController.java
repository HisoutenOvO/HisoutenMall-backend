package cn.hisouten.mall.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.common.pojo.Result;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.pojo.dto.coupon.AdminCouponPageQueryDTO;
import cn.hisouten.mall.pojo.vo.coupon.AdminCouponPageResultVO;
import cn.hisouten.mall.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.NO_PERMISSION;
import static cn.hisouten.mall.constant.RoleConstant.ADMIN;

@RestController("adminCouponController")
@RequestMapping("/admin/coupon")
@Slf4j
@Tag(name = "管理端-优惠券接口")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    /**
     * 分页查询优惠券
     * @param pageQueryDTO 分页条件参数
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询优惠券")
    public Result<PageResult<AdminCouponPageResultVO>> pageQuery(AdminCouponPageQueryDTO pageQueryDTO){
        if (!StpUtil.hasRole(ADMIN)) {
            throw new BizException(NO_PERMISSION);
        }
        Long adminId = StpUtil.getLoginIdAsLong();
        log.info("管理员:{} 分页查询全平台优惠券",adminId);
        PageResult<AdminCouponPageResultVO> pageResult = couponService.adminPageQuery(adminId,pageQueryDTO);
        return Result.success(pageResult);
    }
}
