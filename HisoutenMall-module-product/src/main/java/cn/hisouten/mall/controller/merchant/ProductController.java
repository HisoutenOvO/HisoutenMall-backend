package cn.hisouten.mall.controller.merchant;

import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.product.ProductAddDTO;
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
}
