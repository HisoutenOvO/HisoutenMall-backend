package cn.hisouten.mall.user.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.common.pojo.Result;
import cn.hisouten.mall.pojo.dto.product.UserProductPageQueryDTO;
import cn.hisouten.mall.pojo.vo.product.UserProductPageResultVO;
import cn.hisouten.mall.service.ProductService;
import cn.hisouten.mall.user.pojo.vo.user.UserMerchantDetailVO;
import cn.hisouten.mall.user.pojo.vo.user.UserMerchantFollowPageResultVO;
import cn.hisouten.mall.user.service.MerchantProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/shop")
@Slf4j
@Tag(name = "用户端——店铺接口")
@RequiredArgsConstructor
public class UserMerchantController {
    private final MerchantProfileService merchantProfileService;
    private final ProductService productService;

    /**
     * 查询店铺详情
     * @param merchantId 商家id
     * @return 返回值
     */
    @GetMapping("/{merchantId}")
    @Operation(summary = "查询店铺详情")
    public Result<UserMerchantDetailVO> merchantDetailQuery(@PathVariable Long merchantId){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 查询商家:{} 详情",userId,merchantId);
        UserMerchantDetailVO detailVO = merchantProfileService.userDetailQuery(userId,merchantId);
        return Result.success(detailVO);
    }

    /**
     * 关注店铺
     * @param merchantId 商家id
     * @return 返回值
     */
    @PostMapping("/{merchantId}/follow")
    @Operation(summary = "关注商家")
    public Result follow(@PathVariable Long merchantId){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 关注店铺:{} ",userId,merchantId);
        merchantProfileService.followMerchant(userId,merchantId);
        return Result.success();
    }

    /**
     * 取关店铺
     * @param merchantId 商家id
     * @return 返回值
     */
    @DeleteMapping("/{merchantId}/follow")
    public Result cancelFollow(@PathVariable Long merchantId){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 取消关注店铺:{} ",userId,merchantId);
        merchantProfileService.cancelFollow(userId,merchantId);
        return Result.success();
    }

    /**
     * 分页查询商品信息
     * @param productPageQueryDTO 分页参数
     * @return 返回值
     */
    @GetMapping("/{merchantId}/product/page")
    @Operation(summary = "分页查询商家的商品信息")
    public Result<PageResult<UserProductPageResultVO>> productPageQuery(UserProductPageQueryDTO productPageQueryDTO, @PathVariable String merchantId){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 分页查询商家:{} 的商品信息",userId,merchantId);
        PageResult<UserProductPageResultVO> pageResult = productService.userPageQuery(productPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 查询是否关注店铺
     * @param merchantId 商家id
     * @return 返回值
     */
    @GetMapping("/{merchantId}/check-follow")
    public Result<Boolean> checkFollow(@PathVariable Long merchantId){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 查询是否关注店铺:{} ",userId,merchantId);
        Boolean isFollowed = merchantProfileService.userIsFollowed(userId,merchantId);
        return Result.success(isFollowed);
    }

    @GetMapping("/follow/page")
    @Operation(summary = "分页查询已关注店铺")
    public Result<PageResult<UserMerchantFollowPageResultVO>> followPageQuery(BasePageQueryDTO pageQueryDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 分页查询已关注店铺列表",userId);
        PageResult<UserMerchantFollowPageResultVO> pageResult = merchantProfileService.followPageQuery(userId,pageQueryDTO);
        return Result.success(pageResult);
    }
}
