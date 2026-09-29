package cn.hisouten.mall.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.UserCartAddDTO;
import cn.hisouten.mall.pojo.vo.UserCartItemListVO;
import cn.hisouten.mall.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 用户查询购物车列表
     * @return 返回值
     */
    @GetMapping("/list")
    @Operation(summary = "用户查询购物车列表")
    public Result<List<UserCartItemListVO>> listQuery(){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{}查询购物车列表",userId);
        List<UserCartItemListVO> list = cartService.listQuery(userId);
        return Result.success(list);
    }
}
