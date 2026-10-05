package cn.hisouten.mall.user.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.annotation.Log;
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
     * @return 返回值
     */
    @GetMapping("/profile")
    @Operation(summary = "用户查询详情")
    public Result<UserDetailVO> detailQuery(){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{}查询详情",userId);
        UserDetailVO userDetailVO = userProfileService.detailQuery(userId);
        return Result.success(userDetailVO);
    }

    /**
     * 用户修改个人信息
     * @param userUpdateDTO 修改参数
     * @return 返回值
     */
    @PutMapping("/profile")
    @Log("用户修改个人信息")
    @Operation(summary = "用户修改个人信息")
    public Result updateInfo(@RequestBody UserUpdateDTO userUpdateDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{}修改个人信息",userId);
        userProfileService.updateInfo(userId,userUpdateDTO);
        return Result.success();
    }
}
