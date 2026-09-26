package cn.hisouten.mall.controller.admin;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.brand.AdminBrandPageQueryDTO;
import cn.hisouten.mall.pojo.vo.brand.AdminBrandPageResultVO;
import cn.hisouten.mall.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminBrandController")
@RequiredArgsConstructor
@RequestMapping("/admin/brand")
@Slf4j
@Tag(name = "管理端——品牌接口")
public class BrandController {
    private final BrandService brandService;

    /**
     * 品牌分页查询
     * @param adminBrandPageQueryDTO 分页查询参数
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "品牌分页查询")
    public Result<PageResult<AdminBrandPageResultVO>> pageQuery(AdminBrandPageQueryDTO adminBrandPageQueryDTO){
        log.info("品牌分页查询");
        PageResult<AdminBrandPageResultVO> pageResult = brandService.pageQuery(adminBrandPageQueryDTO);
        return Result.success(pageResult);
    }
}
