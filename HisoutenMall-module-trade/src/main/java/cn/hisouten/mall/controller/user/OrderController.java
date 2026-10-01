package cn.hisouten.mall.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.UserOrderCreateDTO;
import cn.hisouten.mall.pojo.dto.UserOrderPageQueryDTO;
import cn.hisouten.mall.pojo.dto.UserOrderPayDTO;
import cn.hisouten.mall.pojo.vo.UserOrderCreateVO;
import cn.hisouten.mall.pojo.vo.UserOrderPageResultVO;
import cn.hisouten.mall.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Tag(name = "用户端——订单接口")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    /**
     * 用户从购物车里结算下单
     * @param addressId 地址id
     * @return 返回值
     */
    @PostMapping("/create-from-cart")
    @Operation(summary = "从购物车里结算")
    public Result<UserOrderCreateVO> createFromCart(@RequestParam Long addressId){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{}从购物车结算订单",userId);
        UserOrderCreateVO userOrderCreateVO = orderService.createFromCart(userId,addressId);
        return Result.success(userOrderCreateVO);
    }

    /**
     * 用户立即购买下单
     * @param userOrderCreateDTO 下单参数
     * @return 返回值
     */
    @PostMapping("/create-direct")
    @Operation(summary = "直接购买")
    public Result<UserOrderCreateVO> createDirect(@RequestBody UserOrderCreateDTO userOrderCreateDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{}直接下单",userId);
        UserOrderCreateVO userOrderCreateVO = orderService.createDirect(userId,userOrderCreateDTO);
        return Result.success(userOrderCreateVO);
    }

    /**
     * 用户支付订单
     * @param userOrderPayDTO 支付订单参数
     * @return 返回值
     */
    @PostMapping("/pay")
    @Operation(summary = "用户支付订单")
    public Result pay(@RequestBody UserOrderPayDTO userOrderPayDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{}支付订单",userId);
        orderService.pay(userId,userOrderPayDTO);
        return Result.success();
    }


    /**
     * 订单取消支付
     * @param orderNo 订单编号
     * @return 返回值
     */
    @PutMapping("/{orderNo}/cancel")
    @Operation(summary = "订单取消支付")
    public Result cancelPay(@PathVariable String orderNo){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{}取消支付订单",userId);
        orderService.cancelPay(userId,orderNo);
        return Result.success();
    }

    /**
     * 订单分页查询
     * @param userOrderPageQueryDTO 分页查询参数
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "订单分页查询")
    public Result<PageResult<UserOrderPageResultVO>> pageQuery(@RequestBody UserOrderPageQueryDTO userOrderPageQueryDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{}分页查询订单",userId);
        PageResult<UserOrderPageResultVO> pageResult = orderService.pageQuery(userOrderPageQueryDTO);
    }
}
