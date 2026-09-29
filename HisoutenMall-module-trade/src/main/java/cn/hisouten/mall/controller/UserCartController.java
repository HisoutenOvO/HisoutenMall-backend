package cn.hisouten.mall.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.UserCartAddDTO;
import cn.hisouten.mall.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/cart")
@Tag(name = "用户端——购物车接口")
@Slf4j
@RequiredArgsConstructor
public class UserCartController {
    private final CartService cartService;

    /**
     * 用户新增购物车
     * @param userCartAddDTO 新增参数
     * @return 返回值
     */
    @PostMapping
    @Operation(summary = "用户新增购物车")
    public Result addCart(@RequestBody UserCartAddDTO userCartAddDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{}新增购物车商品",userId);
        cartService.addCart(userId,userCartAddDTO);
        return Result.success();
    }


}
