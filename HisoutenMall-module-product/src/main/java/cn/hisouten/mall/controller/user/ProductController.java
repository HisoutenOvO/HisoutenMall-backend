package cn.hisouten.mall.controller.user;

import cn.hisouten.mall.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userProductController")
@RequestMapping("/user/product")
@Slf4j
@Tag(name="用户端——商品接口")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

}
