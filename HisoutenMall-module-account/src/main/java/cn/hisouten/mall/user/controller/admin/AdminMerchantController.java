package cn.hisouten.mall.user.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.annotation.Log;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.common.pojo.Result;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.user.pojo.dto.AdminAuditMerchantDTO;
import cn.hisouten.mall.user.pojo.dto.AdminMerchantPageQueryDTO;
import cn.hisouten.mall.user.pojo.vo.admin.AdminMerchantDetailVO;
import cn.hisouten.mall.user.pojo.vo.admin.AdminMerchantPageResultVO;
import cn.hisouten.mall.user.service.AdminMerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.NO_PERMISSION;
import static cn.hisouten.mall.constant.RoleConstant.ADMIN;

@RestController("adminMerchantController")
@RequestMapping("/admin/merchant")
@Slf4j
@Tag(name = "管理端——商家接口")
@RequiredArgsConstructor
public class AdminMerchantController {
    private final AdminMerchantService adminMerchantService;

    /**
     * 商家分页查询
     * @param adminMerchantPageQueryDTO 分页查询参数
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "商家分页查询")
    public Result<PageResult<AdminMerchantPageResultVO>> pageQuery(@Valid AdminMerchantPageQueryDTO adminMerchantPageQueryDTO){
        if (!StpUtil.hasRole(ADMIN)) {
            throw new BizException(NO_PERMISSION);
        }
        log.info("商家分页查询");
        PageResult<AdminMerchantPageResultVO> pageResult = adminMerchantService.pageQuery(adminMerchantPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 查询商家详情
     * @param merchantId 商家id
     * @return 返回值
     */
    @GetMapping("/{merchantId}")
    @Operation(summary = "查询商家详情")
    public Result<AdminMerchantDetailVO> detailQuery(@PathVariable Long merchantId){
        if (!StpUtil.hasRole(ADMIN)) {
            throw new BizException(NO_PERMISSION);
        }
        log.info("查询商家详情：{}",merchantId);
        AdminMerchantDetailVO adminMerchantDetailVO = adminMerchantService.detailQuery(merchantId);
        return Result.success(adminMerchantDetailVO);
    }

    /**
     * 修改商家状态
     * @param merchantId 商家id
     * @param status 状态
     * @return 返回值
     */
    @PutMapping("/{merchantId}/status")
    @Log("管理端修改商家状态")
    @Operation(summary = "修改商家状态")
    public Result changeStatus(@PathVariable Long merchantId,@RequestParam Integer status){
        if (!StpUtil.hasRole(ADMIN)) {
            throw new BizException(NO_PERMISSION);
        }
        log.info("修改商家状态:{}",merchantId);
        adminMerchantService.changeStatus(merchantId,status);
        return Result.success();
    }

    /**
     * 审核商家
     * @param auditDTO 审核参数
     * @param merchantId 商家id
     * @return 返回值
     */
    @PutMapping("/{merchantId}/audit")
    @Operation(summary = "审核商家")
    public Result auditMerchant(@Valid @RequestBody AdminAuditMerchantDTO auditDTO,@PathVariable Long merchantId){
        if (!StpUtil.hasRole(ADMIN)) {
            throw new BizException(NO_PERMISSION);
        }
        Long adminId = StpUtil.getLoginIdAsLong();
        log.info("管理员:{} 审核了商家:{} ",adminId,merchantId);
        adminMerchantService.auditMerchant(adminId,merchantId,auditDTO);
        return Result.success();
    }
}
