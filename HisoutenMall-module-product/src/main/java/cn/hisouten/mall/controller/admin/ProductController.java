package cn.hisouten.mall.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.common.pojo.Result;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.pojo.dto.product.AdminProductPageQueryDTO;
import cn.hisouten.mall.pojo.vo.product.AdminProductDetailVO;
import cn.hisouten.mall.pojo.vo.product.AdminProductPageResultVO;
import cn.hisouten.mall.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.NO_PERMISSION;
import static cn.hisouten.mall.constant.RoleConstant.ADMIN;

@RestController("adminProductController")
@RequestMapping("/admin/product")
@Slf4j
@Tag(name = "管理端——商品接口")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;


    /**
     * 分页查询商品
     * @param pageQueryDTO 分页条件参数
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询商品")
    public Result<PageResult<AdminProductPageResultVO>> pageQuery(AdminProductPageQueryDTO pageQueryDTO){
        if (!StpUtil.hasRole(ADMIN)) {
            throw new BizException(NO_PERMISSION);
        }
        Long adminId = StpUtil.getLoginIdAsLong();
        log.info("管理员:{} 分页查询商品",adminId);
        PageResult<AdminProductPageResultVO> pageResult = productService.adminPageQuery(adminId,pageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 查询商品详情
     * @param productId 商品id
     * @return 返回值
     */
    @GetMapping("/{productId}")
    @Operation(summary = "管理员查询商品详情")
    public Result<AdminProductDetailVO> detailQuery(@PathVariable Long productId){
        if (!StpUtil.hasRole(ADMIN)) {
            throw new BizException(NO_PERMISSION);
        }
        Long adminId = StpUtil.getLoginIdAsLong();
        log.info("管理员:{} 查询商品:{} 详情",adminId,productId);
        AdminProductDetailVO detailVO = productService.adminDetailQuery(productId);
        return Result.success(detailVO);
    }

    /**
     * 强制下架商品
     * @param productId 商品id
     * @return 返回值
     */
    @PutMapping("/{productId}/offline")
    @Operation(summary = "强制下架商品")
    public Result offlineProduct(@PathVariable Long productId){
        if (!StpUtil.hasRole(ADMIN)) {
            throw new BizException(NO_PERMISSION);
        }
        Long adminId = StpUtil.getLoginIdAsLong();
        log.info("管理员:{} 强制下架商品:{} ",adminId,productId);
        productService.offlineProduct(adminId,productId);
        return Result.success();
    }
}
