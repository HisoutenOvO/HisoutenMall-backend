package cn.hisouten.mall.user.controller;

import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.user.pojo.vo.UserDetailVO;
import cn.hisouten.mall.user.service.AuthService;
import cn.hisouten.mall.user.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/{userId}")
    @Operation(summary = "用户查询详情")
    public Result<UserDetailVO> detailQuery(@PathVariable Long userId){
        log.info("用户：{}查询详情",userId);
        UserDetailVO userDetailVO = userProfileService.detailQuery(userId);
        return Result.success(userDetailVO);
    }
}
