package cn.hisouten.mall.user.controller.merchant;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.annotation.Log;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.user.pojo.dto.MerchantUpdateDTO;
import cn.hisouten.mall.user.pojo.dto.PasswordUpdateDTO;
import cn.hisouten.mall.user.pojo.vo.MerchantDetailVO;
import cn.hisouten.mall.user.service.AuthService;
import cn.hisouten.mall.user.service.MerchantProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
     * @return 返回值
     */
    @GetMapping("/profile")
    @Operation(summary = "商家查询详情")
    public Result<MerchantDetailVO> detailQuery(){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家：{}查询详情",merchantId);
        MerchantDetailVO merchantDetailVO = merchantProfileService.detailQuery(merchantId);
        return Result.success(merchantDetailVO);
    }


    /**
     * 商家修改信息
     * @param merchantUpdateDTO 修改参数
     * @return 返回值
     */
    @PutMapping("/profile")
    @Log("商家端修改信息")
    @Operation(summary = "商家修改信息")
    public Result updateInfo(@RequestBody MerchantUpdateDTO merchantUpdateDTO){
        Long merchantId = StpUtil.getLoginIdAsLong();
        log.info("商家:{}修改信息",merchantId);
        merchantProfileService.updateInfo(merchantId,merchantUpdateDTO);
        return Result.success();
    }
}
