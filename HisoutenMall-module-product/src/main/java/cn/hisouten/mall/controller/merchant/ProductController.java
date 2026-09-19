package cn.hisouten.mall.controller.merchant;

import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.product.ProductAddDTO;
import cn.hisouten.mall.pojo.dto.product.ProductUpdateDTO;
import cn.hisouten.mall.pojo.vo.product.ProductDetailVO;
import cn.hisouten.mall.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController("merchantProductController")
@RequiredArgsConstructor
@RequestMapping("/merchant/product")
@Slf4j
@Tag(name = "商家端——商品接口")
public class ProductController {
    private final ProductService productService;

    /**
     * 新增商品
     * @param productAddDTO 商品内容
     * @return 返回空
     */
    @PostMapping
    @Operation(summary = "新增商品")
    public Result addProduct(@RequestBody ProductAddDTO productAddDTO){
        log.info("商家{}新增商品",productAddDTO.getMerchantId());
        productService.addProduct(productAddDTO);
        return Result.success();
    }

    /**
     * 查询商品详情
     * @param productId 商品ID
     * @return 返回商品详情信息
     */
    @GetMapping("/{productId}")
    @Operation(summary = "查询商品详情")
    public Result<ProductDetailVO> detailQuery(@PathVariable Long productId){
        log.info("查询商品详情：{}",productId);
        ProductDetailVO productDetailVO = productService.detailQuery(productId);
        return Result.success(productDetailVO);
    }

    /**
     * 修改商品
     * @param productId 需要修改商品的id
     * @param productUpdateDTO 修改的数据
     * @return 返回空
     */
    @PutMapping("/{productId}")
    @Operation(summary = "修改商品")
    public Result updateProduct(@PathVariable Long productId, @RequestBody ProductUpdateDTO productUpdateDTO){
        log.info("修改商品：{}",productId);
        productService.updateProduct(productId,productUpdateDTO);
        return Result.success();
    }

    /**
     * 修改商品上下架状态
     * @param productId 商品ID
     * @return 返回空
     */
    @PutMapping("/{productId}/status")
    @Operation(summary = "修改商品上下架状态")
    public Result changeStatus(@PathVariable Long productId,@RequestParam Integer status){
        log.info("修改商品上下架状态：{}",productId);
        productService.changeStatus(productId,status);
        return Result.success();
    }
}
