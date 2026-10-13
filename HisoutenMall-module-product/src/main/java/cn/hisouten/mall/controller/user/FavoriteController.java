package cn.hisouten.mall.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/favorite")
@Slf4j
@Tag(name = "用户端——收藏接口")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    /**
     * 收藏商品
     * @param productId 商品id
     * @return 返回值
     */
    @PostMapping("/{productId}")
    @Operation(summary = "收藏商品")
    public Result addFavorite(@PathVariable Long productId){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 收藏商品:{} ",userId,productId);
        favoriteService.addFavorite(userId,productId);
        return Result.success();
    }

    /**
     * 取消收藏商品
     * @param productId 商品id
     * @return 返回值
     */
    @DeleteMapping("/{productId}")
    @Operation(summary = "取消收藏商品")
    public Result cancelFavorite(@PathVariable Long productId){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{} 取消收藏商品:{} ",userId,productId);
        favoriteService.cancelFavorite(userId,productId);
        return Result.success();
    }
}
