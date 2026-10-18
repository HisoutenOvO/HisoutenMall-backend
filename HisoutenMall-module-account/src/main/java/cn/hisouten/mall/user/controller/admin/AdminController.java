package cn.hisouten.mall.user.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.pojo.Result;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.user.pojo.vo.admin.AdminProfileDetailVO;
import cn.hisouten.mall.user.service.AdminProfileService;
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
@RequestMapping("/admin")
@Slf4j
@Tag(name = "管理端——管理员接口")
@RequiredArgsConstructor
public class AdminController {
    private final AdminProfileService adminProfileService;


    /**
     * 查询详细信息
     * @return 返回值
     */
    @GetMapping("/profile")
    @Operation(summary = "查询详细信息")
    public Result<AdminProfileDetailVO> detailQuery(){
        if(!StpUtil.hasRole(ADMIN)){
            throw new BizException(NO_PERMISSION);
        }
        Long adminId = StpUtil.getLoginIdAsLong();
        log.info("管理员:{} 查询详细信息",adminId);
        AdminProfileDetailVO detailVO = adminProfileService.detailQuery(adminId);
        return Result.success(detailVO);
    }
}
