package cn.hisouten.mall.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.user.pojo.vo.UserAddressListVO;
import cn.hisouten.mall.user.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/address")
@Slf4j
@Tag(name = "用户端——地址接口")
@RequiredArgsConstructor
public class UserAddressController {
    private final UserAddressService userAddressService;

    /**
     * 用户地址列表查询
     * @return 返回值
     */
    @GetMapping("/list")
    @Operation(summary = "用户查询地址列表")
    public Result<List<UserAddressListVO>> listQuery(){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{}查询地址列表", userId);
        List<UserAddressListVO> userAddressListVOS = userAddressService.listQuery(userId);
        return Result.success(userAddressListVOS);
    }
}
