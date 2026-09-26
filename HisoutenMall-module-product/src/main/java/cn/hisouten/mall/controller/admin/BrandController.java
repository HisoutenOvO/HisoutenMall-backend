package cn.hisouten.mall.controller.admin;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.brand.AdminBrandAddDTO;
import cn.hisouten.mall.pojo.dto.brand.AdminBrandPageQueryDTO;
import cn.hisouten.mall.pojo.dto.brand.AdminBrandUpdateDTO;
import cn.hisouten.mall.pojo.vo.brand.AdminBrandDetailVO;
import cn.hisouten.mall.pojo.vo.brand.AdminBrandPageResultVO;
import cn.hisouten.mall.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 查询品牌详情
     * @param brandId 品牌id
     * @return 返回值
     */
    @GetMapping("/{brandId}")
    @Operation(summary = "查询品牌详情")
    public Result<AdminBrandDetailVO> detailQuery(@PathVariable Long brandId){
        log.info("查询品牌详情：{}",brandId);
        AdminBrandDetailVO adminBrandDetailVO = brandService.detailQuery(brandId);
        return Result.success(adminBrandDetailVO);
    }

    /**
     * 新增品牌
     * @param adminBrandAddDTO 新增品牌参数
     * @return 返回值
     */
    @PostMapping
    @Operation(summary = "新增商品")
    public Result addBrand(@RequestBody AdminBrandAddDTO adminBrandAddDTO){
        log.info("新增品牌");
        brandService.addBrand(adminBrandAddDTO);
        return Result.success();
    }

    /**
     * 修改品牌
     * @param adminBrandUpdateDTO 修改参数
     * @return 返回值
     */
    @PutMapping("/{brandId}")
    @Operation(summary = "修改品牌")
    public Result updateBrand(@PathVariable Long brandId ,@RequestBody AdminBrandUpdateDTO adminBrandUpdateDTO){
        log.info("修改品牌：{}",brandId);
        brandService.updateBrand(brandId,adminBrandUpdateDTO);
        return Result.success();
    }

    /**
     * 修改品牌上下架状态
     * @param brandId 品牌id
     * @param status 状态
     * @return 返回值
     */
    @PutMapping("/{brandId}/status")
    @Operation(summary = "修改品牌上下架状态")
    public Result changeStatus(@PathVariable Long brandId,@RequestParam Integer status){
        log.info("修改品牌状态：{}",brandId);
        brandService.changeStatus(brandId,status);
        return Result.success();
    }
}
