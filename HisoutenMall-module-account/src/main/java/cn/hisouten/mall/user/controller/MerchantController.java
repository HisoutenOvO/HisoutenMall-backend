package cn.hisouten.mall.user.controller;

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
     * @param merchantId 商家id
     * @return 返回值
     */
    @GetMapping("/profile/{merchantId}")
    @Operation(summary = "商家查询详情")
    public Result<MerchantDetailVO> detailQuery(@PathVariable Long merchantId){
        log.info("商家：{}查询详情",merchantId);
        MerchantDetailVO merchantDetailVO = merchantProfileService.detailQuery(merchantId);
        return Result.success(merchantDetailVO);
    }


    /**
     * 商家修改信息
     * @param merchantId 商家id
     * @param merchantUpdateDTO 修改参数
     * @return 返回值
     */
    @PutMapping("/profile/{merchantId}")
    @Operation(summary = "商家修改信息")
    public Result updateInfo(@PathVariable Long merchantId, @RequestBody MerchantUpdateDTO merchantUpdateDTO){
        log.info("商家:{}修改信息",merchantId);
        merchantProfileService.updateInfo(merchantId,merchantUpdateDTO);
        return Result.success();
    }


    /**
     * 商家修改密码
     * @param merchantId 商家id
     * @param passwordUpdateDTO 修改密码参数
     * @return 返回值
     */
    @PutMapping("/password/{merchantId}")
    @Operation(summary = "商家修改密码")
    public Result updatePwd(@PathVariable Long merchantId, @RequestBody PasswordUpdateDTO passwordUpdateDTO){
        log.info("商家:{}修改密码",merchantId);
        merchantProfileService.updatePwd(merchantId,passwordUpdateDTO);
        return Result.success();
    }
}
