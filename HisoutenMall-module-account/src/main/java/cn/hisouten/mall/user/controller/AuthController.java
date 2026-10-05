package cn.hisouten.mall.user.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.annotation.Log;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.user.pojo.dto.*;
import cn.hisouten.mall.user.pojo.vo.LoginVO;
import cn.hisouten.mall.user.pojo.vo.UserInfoVO;
import cn.hisouten.mall.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    @Log("商家登录")
    @Operation(summary = "商家登录")
    public Result<LoginVO> merchantLogin(@RequestBody MerchantLoginDTO merchantLoginDTO){
        log.info("商家：'{}'登录",merchantLoginDTO.getUsername());
        LoginVO loginVO = authService.merchantLogin(merchantLoginDTO);
        return Result.success(loginVO);
    }

    /**
     * 商家注册
     * @param merchantRegisterDTO 商家注册参数
     * @return 返回值
     */
    @SaIgnore
    @PostMapping("/merchant/register")
    @Log("商家注册")
    @Operation(summary = "商家注册")
    public Result merchantRegister(@RequestBody MerchantRegisterDTO merchantRegisterDTO){
        log.info("商家注册：{}",merchantRegisterDTO.getUsername());
        authService.merchantRegister(merchantRegisterDTO);
        return Result.success();
    }

    /**
     * 用户登录
     * @param userLoginDTO 用户登录参数
     * @return 返回值
     */
    @SaIgnore
    @PostMapping("/user/login")
    @Log("用户登录")
    @Operation(summary = "用户登录")
    public Result<LoginVO> userLogin(@RequestBody UserLoginDTO userLoginDTO){
        log.info("用户:{}登录",userLoginDTO.getUsername());
        LoginVO loginVO = authService.userLogin(userLoginDTO);
        return Result.success(loginVO);
    }

    /**
     * 管理员登录
     * @param adminLoginDTO 管理员登录参数
     * @return 返回值
     */
    @SaIgnore
    @PostMapping("/admin/login")
    @Log("管理员登录")
    @Operation(summary = "管理员登录")
    public Result<LoginVO> adminLogin(@RequestBody AdminLoginDTO adminLoginDTO){
        log.info("管理员：{}登录",adminLoginDTO.getUsername());
        LoginVO loginVO = authService.adminLogin(adminLoginDTO);
        return Result.success(loginVO);
    }

    /**
     * 用户注册
     * @param userRegisterDTO 用户注册参数
     * @return 返回值
     */
    @SaIgnore
    @PostMapping("/user/register")
    @Log("用户注册")
    @Operation(summary = "用户注册")
    public Result userRegister(@RequestBody UserRegisterDTO userRegisterDTO){
        log.info("用户登录：{}",userRegisterDTO.getUsername());
        authService.userRegister(userRegisterDTO);
        return Result.success();
    }

    /**
     * 通用登出接口
     * @return 返回值
     */
    @PostMapping("/logout")
    @Log("当前用户登出")
    @Operation(summary = "通用登出接口")
    public Result logout(){
        log.info("用户：{}登出", StpUtil.getLoginIdAsLong());
        StpUtil.logout();
        return Result.success();
    }

    /**
     * 返回当前用户信息
     * @return 返回值
     */
    @GetMapping("/info")
    @Operation(summary = "返回当前用户信息")
    public Result<UserInfoVO> infoQuery(){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("返回当前用户：{}信息",userId);
        UserInfoVO userInfoVO = authService.infoQuery(userId);
        return Result.success(userInfoVO);
    }


    /**
     * 修改密码
     * @param passwordUpdateDTO 密码
     * @return 返回值
     */
    @PutMapping("/password")
    @Log("修改密码")
    @Operation(summary = "用户修改密码")
    public Result updatePwd(@RequestBody PasswordUpdateDTO passwordUpdateDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{}修改密码",userId);
        authService.updatePwd(userId, passwordUpdateDTO);
        return Result.success();
    }

}
