package cn.hisouten.mall.controller.merchant;

import cn.hisouten.mall.pojo.Result;
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

@RestController("merchantCategoryController")
@RequestMapping("/merchant/category")
@Slf4j
@Tag(name = "商家端——分类接口")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 分类列表查询
     * @return 返回值
     */
    @GetMapping("/list")
    @Operation(summary = "分类列表查询")
    public Result<List<CategoryListVO>> listQuery(){
        log.info("分类列表查询");
        List<CategoryListVO> categoryListVOList = categoryService.listQuery();
        return Result.success(categoryListVOList);
    }
}
