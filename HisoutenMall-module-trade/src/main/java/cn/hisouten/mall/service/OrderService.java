package cn.hisouten.mall.service;

import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.order.*;
import cn.hisouten.mall.pojo.entity.Order;
import cn.hisouten.mall.pojo.entity.OrderItem;
import cn.hisouten.mall.pojo.vo.logistics.UserLogisticsVO;
import cn.hisouten.mall.pojo.vo.order.*;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;


public interface OrderService {

    /**
     * 用户从购物车里结算下单
     * @param addressId 地址id
     * @param userCouponId 用户优惠券id
     * @return 返回值
     */
    UserOrderCreateVO createFromCart(Long userId, Long addressId,Long userCouponId);

    /**
     * 用户立即购买下单
     * @param userOrderCreateDTO 下单参数
     * @return 返回值
     */
    UserOrderCreateVO createDirect(Long userId, UserOrderCreateDTO userOrderCreateDTO);

    /**
     * 用户支付订单
     * @param userOrderPayDTO 支付订单参数
     */
    void pay(Long userId, UserOrderPayDTO userOrderPayDTO);

    /**
     * 订单取消支付
     * @param orderNo 订单编号
     */
    void cancelPay(Long userId, String orderNo);

    /**
     * 订单分页查询
     * @param userOrderPageQueryDTO 分页查询参数
     * @return 返回值
     */
    PageResult<UserOrderPageResultVO> userPageQuery(UserOrderPageQueryDTO userOrderPageQueryDTO);

    /**
     * 查询订单详情
     * @param orderNo 订单号
     * @return 返回值
     */
    UserOrderDetailVO userDetailQuery(String orderNo);

    /**
     * 商家分页查询订单
     * @param merchantOrderPageQueryDTO 查询条件
     * @return 返回值
     */
    PageResult<MerchantOrderPageResultVO> merchantPageQuery(MerchantOrderPageQueryDTO merchantOrderPageQueryDTO);


    /**
     * 商家查询订单详情
     * @param orderNo 订单号
     * @return 返回值
     */
    MerchantOrderDetailVO merchantDetailQuery(String orderNo);

    /**
     * 商家发货
     * @param orderNo 订单号
     */
    void ship(String orderNo,String company);

    /**
     * 分页查询订单
     * @param adminOrderPageResultDTO 分页参数
     * @return 返回值
     */
    PageResult<AdminOrderPageResultVO> adminPageQuery(AdminOrderPageQueryDTO adminOrderPageResultDTO);

    /**
     * 订单超时关闭逻辑
     * @param orderNo 订单号
     */
    void closeTimeoutOrder(String orderNo);

    /**
     * 新增订单
     * @param order 订单
     */
    void insertOrder(Order order);


    /**
     * 新增订单明细
     * @param item 订单项
     */
    void insertOrderItem(OrderItem item);

    /**
     * 用户查询物流信息
     * @param userId 用户id
     * @param orderNo 订单号
     * @return 返回值
     */
    UserLogisticsVO logisticsQuery(Long userId, String orderNo);

    /**
     * 用户确认收货
     * @param userId 用户id
     * @param orderNo 订单号
     */
    void receive(Long userId, String orderNo);

    /**
     * 管理员查询订单详情
     * @param orderNo 订单号
     * @return 返回值
     */
    AdminOrderDetailVO adminDetailQuery(String orderNo);

    /**
     * 获取商家今日营业额
     * @param merchantId 商家id
     * @return 返回值
     */
    BigDecimal getTodaySales(Long merchantId);


    /**
     * 获取商家今日订单数
     * @param merchantId 商家id
     * @return 返回值
     */
    Long getTodayOrderCount(Long merchantId);

    /**
     * 获取商家累计销售额
     * @param merchantId 商家id
     * @return 返回值
     */
    BigDecimal getTotalSales(Long merchantId);

    /**
     * 获取商家待发货数量
     * @param merchantId 商家id
     * @return 返回值
     */
    @Select("select count(*) from `order` where merchant_id = #{merchantId} and status = 1")
    Long getPendingShipCount(Long merchantId);

    /**
     * 获取今日平台总销售额
     * @return 返回值
     */
    BigDecimal getTodayPlatformSales();

    /**
     * 获取今日平台总订单数
     * @return 返回值
     */
    Long getTodayPlatformOrders();

    /**
     * 获取近7日销售趋势
     * @return 返回值
     */
    List<DailySalesVO> getSalesTrend();
}
