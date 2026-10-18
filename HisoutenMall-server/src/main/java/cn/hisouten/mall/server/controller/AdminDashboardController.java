package cn.hisouten.mall.server.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.pojo.Result;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.server.service.DashboardService;
import cn.hisouten.mall.server.vo.AdminDashboardVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.NO_PERMISSION;
import static cn.hisouten.mall.constant.RoleConstant.ADMIN;

@RestController
@RequestMapping("/admin/dashboard")
@Slf4j
@Tag(name = "管理端——统计接口")
@RequiredArgsConstructor
public class AdminDashboardController {
    private final DashboardService dashboardService;

    /**
     * 查询统计
     * @return 返回值
     */
    @GetMapping("/stats")
    @Operation(summary = "查询统计")
    public Result<AdminDashboardVO> statsQuery(){
        if(!StpUtil.hasRole(ADMIN)){
            throw new BizException(NO_PERMISSION);
        }
        log.info("管理员:{} 查询统计报表",StpUtil.getLoginIdAsLong());
        AdminDashboardVO vo = dashboardService.adminSTatsQuery();
        return Result.success(vo);
    }
}
