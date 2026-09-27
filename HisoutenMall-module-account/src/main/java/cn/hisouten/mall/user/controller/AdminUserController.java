package cn.hisouten.mall.user.controller;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.user.pojo.dto.user.AdminUserPageQueryDTO;
import cn.hisouten.mall.user.pojo.vo.AdminUserPageResultVO;
import cn.hisouten.mall.user.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result<PageResult<AdminUserPageResultVO>> pageQuery(AdminUserPageQueryDTO adminUserPageQueryDTO){
        log.info("用户分页查询");
        PageResult<AdminUserPageResultVO> adminUserPageResultVO = adminUserService.pageQuery(adminUserPageQueryDTO);
        return Result.success(adminUserPageResultVO);
    }
}
