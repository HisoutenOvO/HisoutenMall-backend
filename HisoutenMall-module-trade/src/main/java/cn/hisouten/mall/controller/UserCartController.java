package cn.hisouten.mall.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.UserCartAddDTO;
import cn.hisouten.mall.pojo.dto.UserCartItemDeleteDTO;
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


    /**
     * 购物车修改数量
     * @param cartItemId 购物车单品id
     * @param quantity 最终数量
     * @return 返回值
     */
    @PutMapping("/{cartItemId}/quantity")
    @Operation(summary = "用户修改购物车商品数量")
    public Result updateQuantity(@PathVariable Long cartItemId,@RequestParam Integer quantity){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{}修改购物车数量",userId);
        cartService.updateQuantity(userId,cartItemId,quantity);
        return Result.success();
    }

    /**
     * 用户单条勾选购物车记录
     * @param cartItemId 购物车项id
     * @param checked 勾选状态
     * @return 返回值
     */
    @PutMapping("/{cartItemId}/checked")
    @Operation(summary = "单条勾选")
    public Result updateChecked(@PathVariable Long cartItemId,@RequestParam Integer checked){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("单条勾选购物车项");
        cartService.updateChecked(userId,cartItemId,checked);
        return Result.success();
    }


    /**
     * 全选或全不选
     * @param checked 勾选状态
     * @return 返回值
     */
    @PutMapping("/check-all")
    @Operation(summary = "全选或全不选")
    public Result checkAll(@RequestParam Integer checked){
        log.info("全选或全不选购物车项");
        cartService.checkAll(checked);
        return Result.success();
    }

    /**
     * 用户批量删除购物车数据
     * @param userCartItemDeleteDTO 删除的ids
     * @return 返回值
     */
    @DeleteMapping
    @Operation(summary = "批量删除购物车数据")
    public Result deleteBatch(@RequestBody UserCartItemDeleteDTO userCartItemDeleteDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户批量删除购物车数据:{}",userCartItemDeleteDTO.getIds());
        cartService.deleteBatch(userCartItemDeleteDTO,userId);
        return Result.success();
    }
}
