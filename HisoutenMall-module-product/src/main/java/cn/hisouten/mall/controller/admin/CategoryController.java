package cn.hisouten.mall.controller.admin;

import cn.hisouten.mall.common.annotation.Log;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.category.AdminCategoryAddDTO;
import cn.hisouten.mall.pojo.dto.category.AdminCategoryUpdateDTO;
import cn.hisouten.mall.pojo.vo.brand.AdminBrandDetailVO;
import cn.hisouten.mall.pojo.vo.category.AdminCategoryDetailVO;
import cn.hisouten.mall.pojo.vo.category.AdminCategoryListVO;
import cn.hisouten.mall.pojo.vo.category.AdminCategoryTreeVO;
import cn.hisouten.mall.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminCategoryController")
@RequestMapping("/admin/category")
@Slf4j
@Tag(name = "管理端——分类接口")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 分类列表查询
     * @return 返回值
     */
    @GetMapping("/list")
    @Operation(summary = "分类列表查询")
    public Result<List<AdminCategoryListVO>> listQuery(){
        log.info("分类列表查询");
        List<AdminCategoryListVO> categoryListVOList = categoryService.adminListQuery();
        return Result.success(categoryListVOList);
    }

    /**
     * 分类树形查询
     * @return 返回值
     */
    @GetMapping("/tree")
    @Operation(summary = "分类树形查询")
    public Result<List<AdminCategoryTreeVO>> treeQuery(){
        log.info("分类树形查询");
        List<AdminCategoryTreeVO> adminCategoryTreeVOS = categoryService.treeQuery();
        return Result.success(adminCategoryTreeVOS);
    }

    /**
     * 分类查询详情
     * @param categoryId 分类id
     * @return 返回值
     */
    @GetMapping("/{categoryId}")
    @Operation(summary = "分类查询详情")
    public Result<AdminCategoryDetailVO> detailQuery(@PathVariable Long categoryId){
        log.info("分类查询详情");
        AdminCategoryDetailVO adminCategoryDetailVO = categoryService.detailQuery(categoryId);
        return Result.success(adminCategoryDetailVO);
    }

    /**
     * 新增分类
     * @param adminCategoryAddDTO 分类参数
     * @return 返回值
     */
    @PostMapping
    @Log("管理端新增分类")
    @Operation(summary = "新增分类")
    public Result addCategory(@RequestBody AdminCategoryAddDTO adminCategoryAddDTO){
        log.info("新增分类");
        categoryService.addCategory(adminCategoryAddDTO);
        return Result.success();
    }

    /**
     * 修改分类
     * @param categoryId 分类id
     * @param adminCategoryUpdateDTO 修改分类参数
     * @return 返回值
     */
    @PutMapping("/{categoryId}")
    @Log("管理端修改分类")
    @Operation(summary = "修改分类")
    public Result updateCategory(@PathVariable Long categoryId ,@RequestBody AdminCategoryUpdateDTO adminCategoryUpdateDTO){
        log.info("修改分类:{}",categoryId);
        categoryService.updateCategory(categoryId,adminCategoryUpdateDTO);
        return Result.success();
    }

    /**
     * 修改分类状态
     * @param categoryId 分类id
     * @param status 状态
     * @return 返回值
     */
    @PutMapping("/{categoryId}/status")
    @Log("管理端修改分类状态")
    @Operation(summary = "修改分类状态")
    public Result changStatus(@PathVariable Long categoryId,@RequestParam Integer status){
        log.info("修改分类状态：{}",categoryId);
        categoryService.changeStatus(categoryId,status);
        return Result.success();
    }

    /**
     * 逻辑删除分类
     * @param categoryId 分类id
     * @return 返回值
     */
    @DeleteMapping("/{categoryId}/deleted")
    @Log("管理端逻辑删除分类")
    @Operation(summary = "逻辑删除分类")
    public Result logicDelete(@PathVariable Long categoryId){
        log.info("逻辑删除分类：{}",categoryId);
        categoryService.logicDelete(categoryId);
        return Result.success();
    }
}
