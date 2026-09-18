package cn.hisouten.mall.controller.user;

import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.entity.Category;
import cn.hisouten.mall.pojo.vo.category.CategoryListVO;
import cn.hisouten.mall.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
@Tag(name = "用户端——分类接口")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 用户列表查询分类，用于实现屏幕左侧分类列表
     * @return 返回分类列表
     */
    @GetMapping("/list")
    @Operation(summary = "用户列表查询分类")
    public Result<List<CategoryListVO>> listQuery(){
        log.info("查询分类列表");
        List<CategoryListVO> categoryList = categoryService.listQuery();
        return Result.success(categoryList);
    }
}
