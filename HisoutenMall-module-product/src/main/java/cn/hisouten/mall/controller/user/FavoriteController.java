package cn.hisouten.mall.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.BasePageQueryDTO;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.vo.product.UserFavoritePageResultVO;
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

    /**
     * 分页查询收藏
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询收藏")
    public Result<PageResult<UserFavoritePageResultVO>> pageQuery(BasePageQueryDTO pageQuery){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 分页查询收藏",userId);
        PageResult<UserFavoritePageResultVO> pageResult = favoriteService.pageQuery(pageQuery);
        return Result.success(pageResult);
    }

    /**
     * 判断是否收藏
     * @param productId 商品id
     * @return 返回值
     */
    @GetMapping("/check/{productId}")
    @Operation(summary = "判断是否收藏")
    public Result<Boolean> checkFavorite(@PathVariable Long productId){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{} 是否收藏:{} ?",userId,productId);
        return Result.success(favoriteService.checkFavorite(userId,productId));
    }

    /**
     * 获取收藏数量
     * @return 返回值
     */
    @GetMapping("/count")
    @Operation(summary = "获取收藏数量")
    public Result<Integer> count(){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 获取收藏数量",userId);
        Integer count = favoriteService.count(userId);
        return Result.success(count);
    }
}
