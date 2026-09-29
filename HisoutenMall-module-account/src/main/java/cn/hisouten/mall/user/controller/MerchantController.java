package cn.hisouten.mall.user.controller;

import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.user.pojo.vo.MerchantDetailVO;
import cn.hisouten.mall.user.service.AuthService;
import cn.hisouten.mall.user.service.MerchantProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchant")
@Slf4j
@Tag(name = "商家端——商家接口")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantProfileService merchantProfileService;
    private final AuthService authService;

    /**
     * 商家查询店铺详情
     * @param merchantId 商家id
     * @return 返回值
     */
    @GetMapping("/{merchantId}")
    @Operation(summary = "商家查询详情")
    public Result<MerchantDetailVO> detailQuery(@PathVariable Long merchantId){
        log.info("商家：{}查询详情",merchantId);
        MerchantDetailVO merchantDetailVO = merchantProfileService.detailQuery(merchantId);
        return Result.success(merchantDetailVO);
    }
}
