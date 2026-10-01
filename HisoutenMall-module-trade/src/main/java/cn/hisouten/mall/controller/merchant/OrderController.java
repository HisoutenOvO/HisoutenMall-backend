package cn.hisouten.mall.controller.merchant;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.MerchantOrderPageQueryDTO;
import cn.hisouten.mall.pojo.vo.MerchantOrderDetailVO;
import cn.hisouten.mall.pojo.vo.MerchantOrderPageResultVO;
import cn.hisouten.mall.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController("merchantOrderController")
@RequestMapping("/merchant/order")
@Slf4j
@Tag(name = "商家端——订单接口")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /**
     * 分页查询订单
     * @param merchantOrderPageQueryDTO 查询条件
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询订单")
    public Result<PageResult<MerchantOrderPageResultVO>> pageQuery(MerchantOrderPageQueryDTO  merchantOrderPageQueryDTO){
        log.info("商家：{}分页查询订单", StpUtil.getLoginIdAsLong());
        PageResult<MerchantOrderPageResultVO> pageResult = orderService.merchantPageQuery(merchantOrderPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 商家查询订单详情
     * @param orderNo 订单号
     * @return 返回值
     */
    @GetMapping("/{orderNo}")
    @Operation(summary = "查询订单详情")
    public Result<MerchantOrderDetailVO> detailQuery(@PathVariable String orderNo){
        log.info("商家：{}查询订单详情:{}",StpUtil.getLoginIdAsLong(),orderNo);
        MerchantOrderDetailVO merchantOrderDetailVO = orderService.MerchantDetailQuery(orderNo);
        return Result.success(merchantOrderDetailVO);
    }

    /**
     * 商家发货
     * @param orderNo 订单号
     * @return 返回值
     */
    @PutMapping("/{orderNo}/ship")
    @Operation(summary = "商家发货")
    public Result ship(@PathVariable String orderNo){
        log.info("商家：{}发货:{}",StpUtil.getLoginIdAsLong(),orderNo);
        orderService.ship(orderNo);
        return Result.success();
    }
}
