package cn.hisouten.mall.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.common.pojo.Result;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.pojo.dto.review.AdminReviewPageQueryDTO;
import cn.hisouten.mall.pojo.vo.review.AdminReviewPageResultVO;
import cn.hisouten.mall.pojo.vo.review.ReviewDetailVO;
import cn.hisouten.mall.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.NO_PERMISSION;
import static cn.hisouten.mall.constant.RoleConstant.ADMIN;

@RestController("adminReviewController")
@RequestMapping("/admin/review")
@Slf4j
@Tag(name = "管理端——评价接口")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;


    /**
     * 分页查询评价
     * @param pageQueryDTO 分页条件参数
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询评价")
    public Result<PageResult<AdminReviewPageResultVO>> pageQuery(AdminReviewPageQueryDTO pageQueryDTO){
        if (!StpUtil.hasRole(ADMIN)) {
            throw new BizException(NO_PERMISSION);
        }
        Long adminId = StpUtil.getLoginIdAsLong();
        log.info("管理员:{} 分页查询全平台评价",adminId);
        PageResult<AdminReviewPageResultVO> pageResult = reviewService.adminPageQuery(adminId,pageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 查询评价详情
     * @param reviewId 评价id
     * @return 返回值
     */
    @GetMapping("/{reviewId}")
    @Operation(summary = "查询评价详情")
    public Result<ReviewDetailVO> detailQuery(@PathVariable Long reviewId){
        if (!StpUtil.hasRole(ADMIN)) {
            throw new BizException(NO_PERMISSION);
        }
        Long adminId = StpUtil.getLoginIdAsLong();
        log.info("管理员:{} 查询评价详情:{} ",adminId,reviewId);
        ReviewDetailVO detailVO = reviewService.detailQuery(adminId,reviewId);
        return Result.success(detailVO);
    }

    /**
     * 强制删除评论
     * @param reviewId 评价id
     * @return 返回值
     */
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "强制删除评论")
    public Result deleteReview(@PathVariable Long reviewId){
        if (!StpUtil.hasRole(ADMIN)) {
            throw new BizException(NO_PERMISSION);
        }
        Long adminId = StpUtil.getLoginIdAsLong();
        log.info("管理员:{} 强制删除评论:{} ",adminId,reviewId);
        reviewService.adminDeleteReview(adminId,reviewId);
        return Result.success();
    }
}
