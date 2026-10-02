package cn.hisouten.mall.user.controller.admin;

import cn.hisouten.mall.common.annotation.Log;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.user.pojo.dto.AdminMerchantPageQueryDTO;
import cn.hisouten.mall.user.pojo.vo.AdminMerchantDetailVO;
import cn.hisouten.mall.user.pojo.vo.AdminMerchantPageResultVO;
import cn.hisouten.mall.user.service.AdminMerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public Result<PageResult<AdminMerchantPageResultVO>> pageQuery(AdminMerchantPageQueryDTO adminMerchantPageQueryDTO){
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
    @PostMapping("/{merchantId}/status")
    @Log("管理端修改商家状态")
    @Operation(summary = "修改商家状态")
    public Result changeStatus(@PathVariable Long merchantId,@RequestParam Integer status){
        log.info("修改商家状态:{}",merchantId);
        adminMerchantService.changeStatus(merchantId,status);
        return Result.success();
    }
}
