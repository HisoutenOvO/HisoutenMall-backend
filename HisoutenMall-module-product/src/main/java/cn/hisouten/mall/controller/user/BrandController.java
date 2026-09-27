package cn.hisouten.mall.controller.user;

import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.vo.brand.BrandListVO;
import cn.hisouten.mall.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userBrandController")
@RequestMapping("/user/brand")
@Slf4j
@Tag(name = "用户端——品牌接口")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    /**
     * 用户列表查询品牌
     * @return 返回值
     */
    @GetMapping("/list")
    @Operation(summary = "用户列表查询品牌")
    public Result<List<BrandListVO>> listQuery(){
        log.info("用户列表查询");
        List<BrandListVO> brandListVOList = brandService.listQuery();
        return Result.success(brandListVOList);
    }
}
