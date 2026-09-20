package cn.hisouten.mall.controller.merchant;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.product.MerchantProductAddDTO;
import cn.hisouten.mall.pojo.dto.product.MerchantProductPageQueryDTO;
import cn.hisouten.mall.pojo.dto.product.MerchantProductUpdateDTO;
import cn.hisouten.mall.pojo.vo.product.MerchantProductDetailVO;
import cn.hisouten.mall.pojo.vo.product.MerchantProductListVO;
import cn.hisouten.mall.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
     * 分页查询商品
     * @param merchantProductPageQueryDTO 分页查询参数
     * @return 返回分页查询结果
     */
    @GetMapping("/page")
    @Operation(summary = "商品分页查询")
    public Result<PageResult<MerchantProductListVO>> pageQuery(MerchantProductPageQueryDTO merchantProductPageQueryDTO){
        log.info("分页查询商品信息");
        PageResult<MerchantProductListVO> pageResult = productService.merchantPageQuery(merchantProductPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 新增商品
     * @param merchantProductAddDTO 商品内容
     * @return 返回空
     */
    @PostMapping
    @Operation(summary = "新增商品")
    public Result addProduct(@Valid @RequestBody MerchantProductAddDTO merchantProductAddDTO){
        log.info("商家{}新增商品", merchantProductAddDTO.getMerchantId());
        productService.addProduct(merchantProductAddDTO);
        return Result.success();
    }

    /**
     * 查询商品详情
     * @param productId 商品ID
     * @return 返回商品详情信息
     */
    @GetMapping("/{productId}")
    @Operation(summary = "查询商品详情")
    public Result<MerchantProductDetailVO> detailQuery(@PathVariable Long productId){
        log.info("查询商品详情：{}",productId);
        MerchantProductDetailVO merchantProductDetailVO = productService.merchantDetailQuery(productId);
        return Result.success(merchantProductDetailVO);
    }

    /**
     * 修改商品
     * @param productId 需要修改商品的id
     * @param merchantProductUpdateDTO 修改的数据
     * @return 返回空
     */
    @PutMapping("/{productId}")
    @Operation(summary = "修改商品")
    public Result updateProduct(@PathVariable Long productId,@Valid @RequestBody MerchantProductUpdateDTO merchantProductUpdateDTO){
        log.info("修改商品：{}",productId);
        productService.updateProduct(productId, merchantProductUpdateDTO);
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

    /**
     * 逻辑删除商品
     * @param productId 商品id
     * @return 返回空
     */
    @DeleteMapping("/{productId}/deleted")
    @Operation(summary = "逻辑删除商品")
    public Result logicDeleteProduct(@PathVariable Long productId){
        log.info("逻辑删除商品：{}",productId);
        productService.logicDeleteProduct(productId);
        return Result.success();
    }


    /**
     * 恢复逻辑删除的商品
     * @param productId 商品id
     * @return 返回空
     */
    @PutMapping("/{productId}/deleted")
    @Operation(summary = "复原删除的商品")
    public Result recoveryProduct(@PathVariable Long productId){
        log.info("复原商品：{}",productId);
        productService.recoveryProduct(productId);
        return Result.success();
    }

    /**
     * 彻底删除商品数据
     * @param productId 商品id
     * @return 返回空
     */
    @DeleteMapping("/{productId}")
    @Operation(summary = "删除商品数据")
    public Result deleteProduct(@PathVariable Long productId){
        log.info("删除商品数据:{}",productId);
        productService.deleteProduct(productId);
        return Result.success();
    }
}
