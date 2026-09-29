package cn.hisouten.mall.user.controller;

import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.user.pojo.dto.UserUpdateDTO;
import cn.hisouten.mall.user.pojo.dto.PasswordUpdateDTO;
import cn.hisouten.mall.user.pojo.vo.UserDetailVO;
import cn.hisouten.mall.user.service.AuthService;
import cn.hisouten.mall.user.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "用户端——用户接口")
@RequiredArgsConstructor
public class UserController {
    private final UserProfileService userProfileService;
    private final AuthService authService;

    /**
     * 用户查询详情
     * @param userId 用户id
     * @return 返回值
     */
    @GetMapping("/profile/{userId}")
    @Operation(summary = "用户查询详情")
    public Result<UserDetailVO> detailQuery(@PathVariable Long userId){
        log.info("用户：{}查询详情",userId);
        UserDetailVO userDetailVO = userProfileService.detailQuery(userId);
        return Result.success(userDetailVO);
    }

    /**
     * 用户修改个人信息
     * @param userId 用户id
     * @param userUpdateDTO 修改参数
     * @return 返回值
     */
    @PutMapping("/profile/{userId}")
    @Operation(summary = "用户修改个人信息")
    public Result updateInfo(@PathVariable Long userId, @RequestBody UserUpdateDTO userUpdateDTO){
        log.info("用户：{}修改个人信息",userId);
        userProfileService.updateInfo(userId,userUpdateDTO);
        return Result.success();
    }

    /**
     * 用户修改密码
     * @param userId 用户id
     * @param passwordUpdateDTO 密码
     * @return 返回值
     */
    @PutMapping("/password/{userId}")
    @Operation(summary = "用户修改密码")
    public Result updatePwd(@PathVariable Long userId, @RequestBody PasswordUpdateDTO passwordUpdateDTO){
        log.info("用户：{}修改密码",userId);
        userProfileService.updatePwd(userId, passwordUpdateDTO);
        return Result.success();
    }
}
