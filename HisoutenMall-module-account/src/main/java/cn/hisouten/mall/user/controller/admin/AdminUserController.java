package cn.hisouten.mall.user.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.annotation.Log;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.common.pojo.Result;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.user.pojo.dto.AdminUserPageQueryDTO;
import cn.hisouten.mall.user.pojo.vo.admin.AdminUserPageResultVO;
import cn.hisouten.mall.user.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.NO_PERMISSION;
import static cn.hisouten.mall.constant.RoleConstant.ADMIN;

@RestController("adminUserController")
@RequestMapping("/admin/user")
@Slf4j
@Tag(name = "管理端——用户接口")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    /**
     * 用户分页查询
     * @param adminUserPageQueryDTO 分页查询参数
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "用户分页查询")
    public Result<PageResult<AdminUserPageResultVO>> pageQuery(@Valid AdminUserPageQueryDTO adminUserPageQueryDTO){
        if (!StpUtil.hasRole(ADMIN)) {
            throw new BizException(NO_PERMISSION);
        }
        log.info("用户分页查询");
        PageResult<AdminUserPageResultVO> adminUserPageResultVO = adminUserService.pageQuery(adminUserPageQueryDTO);
        return Result.success(adminUserPageResultVO);
    }

    /**
     * 改变用户状态
     * @param userId 用户id
     * @param status 状态
     * @return 返回值
     */
    @PutMapping("/{userId}/status")
    @Log("管理端修改用户状态")
    @Operation(summary = "改变用户状态")
    public Result changeStatus(@PathVariable Long userId,@RequestParam Integer status){
        if (!StpUtil.hasRole(ADMIN)) {
            throw new BizException(NO_PERMISSION);
        }
        log.info("改变用户状态：{}",userId);
        adminUserService.changeStatus(userId,status);
        return Result.success();
    }
}
