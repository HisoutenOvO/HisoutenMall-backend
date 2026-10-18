package cn.hisouten.mall.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.annotation.RepeatSubmit;
import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.common.pojo.Result;
import cn.hisouten.mall.pojo.dto.review.UserReviewAddDTO;
import cn.hisouten.mall.pojo.dto.review.ReviewPageQueryDTO;
import cn.hisouten.mall.pojo.vo.review.UserPendingReviewPageResultVO;
import cn.hisouten.mall.pojo.vo.review.UserReviewPageResultVO;
import cn.hisouten.mall.pojo.vo.review.UserSelfReviewPageResultVO;
import cn.hisouten.mall.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController("userReviewController")
@RequestMapping("/user/review")
@Slf4j
@Tag(name = "用户端——评价接口")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 新增评价
     * @param addDTO 评价参数
     * @return 返回值
     */
    @PostMapping
    @RepeatSubmit
    @Operation(summary = "新增评价")
    public Result addReview(@Valid @RequestBody UserReviewAddDTO addDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 新增评价",userId);
        reviewService.addReview(userId,addDTO);
        return Result.success();
    }

    /**
     * 用户端游客查询评价列表
     * @param pageQueryDTO 查询参数
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询评价")
    public Result<PageResult<UserReviewPageResultVO>> pageQuery(@Valid ReviewPageQueryDTO pageQueryDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 分页查询评价",userId);
        PageResult<UserReviewPageResultVO> pageResult = reviewService.userPageQuery(pageQueryDTO);
        return Result.success(pageResult);

    }

    /**
     * 用户分页查询自己发过的评价列表
     * @param pageQueryDTO 查询参数
     * @return 返回值
     */
    @GetMapping("/self/page")
    @Operation(summary = "用户分页查询自己的评价")
    public Result<PageResult<UserSelfReviewPageResultVO>> selfPageQuery(@Valid ReviewPageQueryDTO pageQueryDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 分页查询自己发表过的评价",userId);
        PageResult<UserSelfReviewPageResultVO> pageResult = reviewService.selfPageQuery(userId,pageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除评价
     * @param reviewId 评价id
     * @return 返回值
     */
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "删除评价")
    public Result deleteReview(@PathVariable Long reviewId){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 删除评价:{} ",userId,reviewId);
        reviewService.deleteReview(userId,reviewId);
        return Result.success();
    }

    /**
     * 分页查询待评价列表
     * @param pageQueryDTO 分页条件
     * @return 返回值
     */
    @GetMapping("/pending")
    @Operation(summary = "分页查询待支付列表")
    public Result<PageResult<UserPendingReviewPageResultVO>> pendingQuery(@Valid BasePageQueryDTO pageQueryDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 分页查询待评价列表",userId);
        PageResult<UserPendingReviewPageResultVO> pageResult = reviewService.pendingPageQuery(userId,pageQueryDTO);
        return Result.success(pageResult);
    }
}
