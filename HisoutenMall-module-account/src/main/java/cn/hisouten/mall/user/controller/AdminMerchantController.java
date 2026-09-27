package cn.hisouten.mall.user.controller;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.user.pojo.dto.AdminMerchantPageQueryDTO;
import cn.hisouten.mall.user.pojo.vo.AdminMerchantPageResultVO;
import cn.hisouten.mall.user.service.AdminMerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
