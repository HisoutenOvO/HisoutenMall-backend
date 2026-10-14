package cn.hisouten.mall.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.UserReviewAddDTO;
import cn.hisouten.mall.pojo.dto.UserReviewPageQueryDTO;
import cn.hisouten.mall.pojo.vo.UserReviewPageResultVO;
import cn.hisouten.mall.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
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
    @Operation(summary = "新增评价")
    public Result addReview(@RequestBody UserReviewAddDTO addDTO){
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
    public Result<PageResult<UserReviewPageResultVO>> pageQuery(UserReviewPageQueryDTO pageQueryDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 分页查询评价",userId);
        PageResult<UserReviewPageResultVO> pageResult = reviewService.userPageResult(pageQueryDTO);
        return Result.success(pageResult);

    }




}
