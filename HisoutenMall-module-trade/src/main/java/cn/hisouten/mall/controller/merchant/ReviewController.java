package cn.hisouten.mall.controller.merchant;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.ReviewPageQueryDTO;
import cn.hisouten.mall.pojo.vo.MerchantReviewPageResultVO;
import cn.hisouten.mall.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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
    public Result<PageResult<MerchantReviewPageResultVO>> pageQuery(ReviewPageQueryDTO pageQueryDTO){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家:{} 分页查询自己的评价记录",merchantId);
        PageResult<MerchantReviewPageResultVO> pageResult = reviewService.merchantPageResult(merchantId,pageQueryDTO);
        return Result.success(pageResult);
    }
}
