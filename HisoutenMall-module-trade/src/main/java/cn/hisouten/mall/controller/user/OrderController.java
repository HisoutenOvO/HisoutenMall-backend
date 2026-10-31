package cn.hisouten.mall.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.annotation.Log;
import cn.hisouten.mall.common.annotation.RepeatSubmit;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.common.pojo.Result;
import cn.hisouten.mall.pojo.dto.order.*;
import cn.hisouten.mall.pojo.vo.logistics.UserLogisticsVO;
import cn.hisouten.mall.pojo.vo.order.UserOrderCreateVO;
import cn.hisouten.mall.pojo.vo.order.UserOrderDetailVO;
import cn.hisouten.mall.pojo.vo.order.UserOrderPageResultVO;
import cn.hisouten.mall.pojo.vo.order.UserOrderPreviewVO;
import cn.hisouten.mall.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Tag(name = "用户端——订单接口")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    /**
     * 用户从购物车里结算下单
     * @param dto 下单参数
     * @return 返回值
     */
    @PostMapping("/create-from-cart")
    @Log("购物车下单")
    @RepeatSubmit(interval = 3) //3秒防重复
    @Operation(summary = "从购物车里结算")
    public Result<UserOrderCreateVO> createFromCart(@RequestBody UserOrderFromCartDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{}从购物车结算订单", userId);
        UserOrderCreateVO userOrderCreateVO = orderService.createFromCart(userId, dto.getAddressId(),dto.getUserCouponIds());
        return Result.success(userOrderCreateVO);
    }

    /**
     * 用户立即购买下单
     * @param userOrderCreateDTO 下单参数
     * @return 返回值
     */
    @PostMapping("/create-direct")
    @Log("立即下单")
    @RepeatSubmit(interval = 3)
    @Operation(summary = "直接购买")
    public Result<UserOrderCreateVO> createDirect(@Valid @RequestBody UserOrderCreateDTO userOrderCreateDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{}直接下单", userId);
        UserOrderCreateVO userOrderCreateVO = orderService.createDirect(userId, userOrderCreateDTO);
        return Result.success(userOrderCreateVO);
    }

    /**
     * 立即购买预计算下单信息
     * @param dto 参数
     * @return 返回值
     */
    @PostMapping("/preview-direct")
    @Operation(summary = "立即购买预计算")
    public Result<UserOrderPreviewVO> previewDirect(@Valid @RequestBody UserOrderPreviewDirectDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 直接购买商品预计算下单信息",userId);
        return Result.success(orderService.previewDirect(userId, dto));
    }

    /**
     * 预计算订单信息
     * @param previewDTO 预计算参数
     * @return 返回值
     */
    @PostMapping("/preview")
    @Operation(summary = "预计算订单信息")
    public Result<UserOrderPreviewVO> previewOrder(@RequestBody UserOrderPreviewDTO previewDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 预计算订单信息",userId);
        UserOrderPreviewVO previewVO = orderService.previewOrder(userId,previewDTO);
        return Result.success(previewVO);
    }

    /**
     * 用户修改订单地址
     * @param orderNo 订单号
     * @param addressId 地址id
     * @return 返回值
     */
    @PutMapping("/{orderNo}/address")
    @Operation(summary = "修改订单收货地址")
    public Result updateOrderAddress(@PathVariable String orderNo, @RequestParam Long addressId) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{} 修改订单:{} 的地址",userId,orderNo);
        orderService.updateOrderAddress(userId, orderNo, addressId);
        return Result.success();
    }

    /**
     * 订单取消支付
     * @param orderNo 订单编号
     * @return 返回值
     */
    @PutMapping("/{orderNo}/cancel")
    @Log("取消支付订单")
    @Operation(summary = "订单取消支付")
    public Result cancelPay(@PathVariable String orderNo) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{}取消支付订单", userId);
        orderService.cancelPay(userId, orderNo);
        return Result.success();
    }

    /**
     * 订单分页查询
     * @param userOrderPageQueryDTO 分页查询参数
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "订单分页查询")
    public Result<PageResult<UserOrderPageResultVO>> pageQuery(@Valid UserOrderPageQueryDTO userOrderPageQueryDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{}分页查询订单", userId);
        PageResult<UserOrderPageResultVO> pageResult = orderService.userPageQuery(userOrderPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 查询订单详情
     * @param orderNo 订单号
     * @return 返回值
     */
    @GetMapping("/{orderNo}")
    @Operation(summary = "查询订单详情")
    public Result<UserOrderDetailVO> detailQuery(@PathVariable String orderNo){
        log.info("用户:{}查询订单详情",StpUtil.getLoginIdAsLong());
        UserOrderDetailVO userDetailVO = orderService.userDetailQuery(orderNo);
        return Result.success(userDetailVO);
    }

    /**
     * 查询物流信息
     * @param orderNo 订单信息
     * @return 返回值
     */
    @GetMapping("/{orderNo}/logistics")
    @Operation(summary = "查询物流信息")
    public Result<UserLogisticsVO> logisticsQuery(@PathVariable String orderNo){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 查询物流",userId);
        UserLogisticsVO vo = orderService.logisticsQuery(userId,orderNo);
        return Result.success(vo);
    }

    /**
     * 确认收货
     * @param orderNo 订单号
     * @return 返回值
     */
    @PutMapping("/{orderNo}/receive")
    @Operation(summary = "确认收货")
    public Result receive(@PathVariable String orderNo){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 确认收货:{} ",userId,orderNo);
        orderService.receive(userId,orderNo);
        return Result.success();
    }

    /**
     * 删除订单
     * @param orderNo 订单号
     * @return 返回值
     */
    @DeleteMapping("/{orderNo}")
    @Operation(summary = "用户删除订单")
    public Result delete(@PathVariable String orderNo){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 删除订单:{} ",userId,orderNo);
        orderService.userDeleteOrder(userId,orderNo);
        return Result.success();
    }

    /**
     * 用户恢复删除订单
     * @param orderNo 订单号
     * @return 返回值
     */
    @PutMapping("/{orderNo}/restore")
    @Operation(summary = "恢复订单")
    public Result restoreOrder(@PathVariable String orderNo) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 恢复订单:{}", userId, orderNo);
        orderService.restoreOrder(userId, orderNo);
        return Result.success();
    }
}
