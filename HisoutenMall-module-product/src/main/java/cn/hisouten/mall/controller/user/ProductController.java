package cn.hisouten.mall.controller.user;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.product.UserProductPageQueryDTO;
import cn.hisouten.mall.pojo.vo.product.UserProductDetailVO;
import cn.hisouten.mall.pojo.vo.product.UserProductPageResultVO;
import cn.hisouten.mall.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userProductController")
@RequestMapping("/user/product")
@Slf4j
@Tag(name="用户端——商品接口")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * 分页查询商品
     * @return 返回字段
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询商品")
    public Result<PageResult<UserProductPageResultVO>> pageQuery(UserProductPageQueryDTO userProductPageQueryDTO){
        log.info("分页查询商品");
        PageResult<UserProductPageResultVO> pageQuery = productService.userPageQuery(userProductPageQueryDTO);
        return Result.success(pageQuery);
    }

    /**
     * 查询商品详情
     * @param productId 商品ID
     * @return 返回商品详情信息
     */
    @GetMapping("/{productId}")
    @Operation(summary = "查询商品详情")
    public Result<UserProductDetailVO> detailQuery(@PathVariable Long productId){
        log.info("查询商品详情：{}",productId);
        UserProductDetailVO userProductDetailVO = productService.userDetailQuery(productId);
        return Result.success(userProductDetailVO);
    }

}
