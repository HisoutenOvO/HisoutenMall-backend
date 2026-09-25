package cn.hisouten.mall.user.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.user.pojo.dto.merchant.MerchantLoginDTO;
import cn.hisouten.mall.user.pojo.vo.LoginVO;
import cn.hisouten.mall.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "通用——登录接口")
@Slf4j
public class AuthController {
    private final AuthService authService;

    /**
     * 商家登录
     * @param merchantLoginDTO 商家登录参数
     * @return 返回值
     */
    @SaIgnore
    @PostMapping("/merchant/login")
    @Operation(summary = "商家登录")
    public Result<LoginVO> merchantLogin(@RequestBody MerchantLoginDTO merchantLoginDTO){
        log.info("商家：'{}'登录",merchantLoginDTO.getUsername());
        LoginVO loginVO = authService.merchantLogin(merchantLoginDTO);
        return Result.success(loginVO);
    }
}
