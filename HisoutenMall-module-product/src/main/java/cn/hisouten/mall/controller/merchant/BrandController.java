package cn.hisouten.mall.controller.merchant;

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

@RequestMapping("/merchant/brand")
@RequiredArgsConstructor
@Tag(name = "商家端——品牌接口")
@Slf4j
@RestController("merchantBrandController")
public class BrandController {
    private final BrandService brandService;

    /**
     * 获取品牌列表
     * @return 返回值
     */
    @GetMapping("/list")
    @Operation(summary = "获取品牌列表")
    public Result<List<BrandListVO>> listQuery(){
        log.info("获取品牌列表");
        List<BrandListVO> brandListVOList = brandService.listQuery();
        return Result.success(brandListVOList);
    }
}
