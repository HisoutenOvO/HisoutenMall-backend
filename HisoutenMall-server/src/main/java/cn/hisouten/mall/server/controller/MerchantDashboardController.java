package cn.hisouten.mall.server.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.pojo.Result;
import cn.hisouten.mall.server.service.DashboardService;
import cn.hisouten.mall.server.vo.MerchantDashboardVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchant/dashboard")
@Slf4j
@Tag(name = "商家端——统计接口")
@RequiredArgsConstructor
public class MerchantDashboardController {
    private final DashboardService dashboardService;

    /**
     * 获取统计信息
     * @return 返回值
     */
    @GetMapping("/stats")
    @Operation(summary = "获取统计信息")
    public Result<MerchantDashboardVO> statsQuery(){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家:{} 获取统计信息",merchantId);
        MerchantDashboardVO vo = dashboardService.merchantStatsQuery(merchantId);
        return Result.success(vo);
    }
}
