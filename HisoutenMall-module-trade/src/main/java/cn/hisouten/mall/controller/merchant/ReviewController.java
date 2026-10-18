package cn.hisouten.mall.controller.merchant;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.common.pojo.Result;
import cn.hisouten.mall.pojo.dto.review.ReviewPageQueryDTO;
import cn.hisouten.mall.pojo.vo.review.MerchantReviewPageResultVO;
import cn.hisouten.mall.pojo.vo.review.MerchantReviewStatisticsVO;
import cn.hisouten.mall.pojo.vo.review.ReviewDetailVO;
import cn.hisouten.mall.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("merchantReviewController")
@RequestMapping("/merchant/review")
@Tag(name = "商家端——评价接口")
@Slf4j
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 分页查询评价列表
     * @param pageQueryDTO 查询参数
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询评价")
    public Result<PageResult<MerchantReviewPageResultVO>> pageQuery(@Valid ReviewPageQueryDTO pageQueryDTO){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家:{} 分页查询自己的评价记录",merchantId);
        PageResult<MerchantReviewPageResultVO> pageResult = reviewService.merchantPageQuery(merchantId,pageQueryDTO);
        return Result.success(pageResult);
    }



    @GetMapping("/{reviewId}")
    @Operation(summary = "查询评价详情")
    public Result<ReviewDetailVO> detailQuery(@PathVariable Long reviewId){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家:{} 查询评价详情:{} ",merchantId,reviewId);
        ReviewDetailVO detailVO = reviewService.detailQuery(merchantId,reviewId);
        return Result.success(detailVO);
    }

    /**
     * 评价统计
     * @param productId 商品id
     * @return 返回值
     */
    @GetMapping("/{productId}/statistics")
    @Operation(summary = "评价统计")
    public Result<MerchantReviewStatisticsVO> statisticsQuery(@PathVariable Long productId){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 评价统计",merchantId);
        MerchantReviewStatisticsVO statisticsVO = reviewService.statisticsQuery(merchantId,productId);
        return Result.success(statisticsVO);
    }
}
