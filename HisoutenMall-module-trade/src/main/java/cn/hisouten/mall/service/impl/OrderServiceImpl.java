package cn.hisouten.mall.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.*;
import cn.hisouten.mall.mq.message.OrderCancelMessage;
import cn.hisouten.mall.mq.message.OrderTimeoutMessage;
import cn.hisouten.mall.mq.producer.MqProducerService;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.pojo.bo.cart.CartItemListBO;
import cn.hisouten.mall.pojo.dto.order.*;
import cn.hisouten.mall.pojo.entity.*;
import cn.hisouten.mall.pojo.vo.coupon.OrderCouponVO;
import cn.hisouten.mall.pojo.vo.logistics.UserLogisticsVO;
import cn.hisouten.mall.pojo.vo.order.*;
import cn.hisouten.mall.service.OrderService;
import cn.hisouten.mall.service.ProductService;
import cn.hisouten.mall.user.pojo.entity.UserAddress;
import cn.hisouten.mall.user.service.MerchantProfileService;
import cn.hisouten.mall.user.service.UserAddressService;
import cn.hisouten.mall.user.service.UserProfileService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static cn.hisouten.mall.constant.CommonConstant.AMOUNT_OFF_COUPON;
import static cn.hisouten.mall.constant.CommonConstant.DEFAULT_LOGISTICS_COMPANY;
import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.PayMethodConstant.ALIPAY;
import static cn.hisouten.mall.constant.PayMethodConstant.WECHAT;
import static cn.hisouten.mall.constant.SeckillConstant.NORMAL;
import static cn.hisouten.mall.constant.SeckillConstant.SECKILL;
import static cn.hisouten.mall.constant.StatusConstant.*;
import static cn.hisouten.mall.mq.constant.MqConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final CartItemMapper cartItemMapper;
    private final OrderItemMapper orderItemMapper;
    private final PaymentMapper paymentMapper;
    private final LogisticsMapper logisticsMapper;
    private final UserCouponMapper userCouponMapper;
    private final CouponMapper couponMapper;

    private final UserAddressService userAddressService;
    private final ProductService productService;
    private final MerchantProfileService merchantProfileService;
    private final UserProfileService userProfileService;
    private final MqProducerService mqProducerService;


    /**
     * 用户从购物车里结算下单
     * @param addressId 地址id
     * @param userCouponId 用户优惠券id
     * @return 返回值
     */
    @Override
    @Transactional
    public UserOrderCreateVO createFromCart(Long userId, Long addressId,Long userCouponId) {
        //查看购物车勾选项
        List<CartItemListBO> cartItemList = cartItemMapper.selectCheckedItemByUserId(userId);
        if (cartItemList == null || cartItemList.isEmpty()) {
            throw new BizException(CHECK_PRODUCT_FIRST);
        }
        //判断有效
        for (CartItemListBO bo : cartItemList) {
            if (bo.getSkuDeleted() == ENABLED || bo.getSkuStatus() != ENABLED) {
                throw new BizException(SKU_HAS_DISABLED);
            } else if (bo.getProductDeleted() == ENABLED || bo.getProductStatus() != ENABLED) {
                throw new BizException(PRODUCT_HAS_DISABLED);
            } else if (bo.getStock() < bo.getQuantity()) {
               throw new BizException(OUT_OF_STOCK);
            }
        }

        //获取下单后清除的购物车项id
        List<Long> clearIds = cartItemList.stream().map(CartItemListBO::getId).toList();
        return doCreateOrder(userId,addressId,cartItemList,clearIds,userCouponId);
    }

    /**
     * 用户立即购买下单
     * @param userOrderCreateDTO 下单参数
     * @return 返回值
     */
    @Override
    @Transactional
    public UserOrderCreateVO createDirect(Long userId, UserOrderCreateDTO userOrderCreateDTO) {
        //判断有效性
        ProductSku sku = productService.getSkuBySkuId(userOrderCreateDTO.getSkuId());
        if (sku == null) {
            throw new BizException(SKU_NOT_FOUND);
        }
        Product product = productService.getProductByProductId(sku.getProductId());
        if(product == null){
            throw new BizException(PRODUCT_NOT_FOUND);
        }if (sku.getStatus() != ENABLED) {
            throw new BizException(SKU_HAS_DISABLED);
        }
        if (product.getStatus() != ENABLED) {
            throw new BizException(PRODUCT_HAS_DISABLED);
        }
        if(sku.getStock() < userOrderCreateDTO.getQuantity()){
            throw new BizException(OUT_OF_STOCK);
        }

        //组装成bo给核心下单逻辑
        CartItemListBO item = new CartItemListBO();
        item.setProductId(product.getId());
        item.setSkuId(sku.getId());
        item.setMerchantId(product.getMerchantId());
        item.setProductName(product.getName());
        item.setSkuSpecs(sku.getSpecs());
        item.setPrice(sku.getPrice());
        item.setQuantity(userOrderCreateDTO.getQuantity());
        return doCreateOrder(userId,userOrderCreateDTO.getAddressId(),List.of(item),null,userOrderCreateDTO.getUserCouponId());
    }

    /**
     * 支付成功逻辑
     * @param orderNo 订单号
     */
    @Override
    @Transactional
    public void paySuccess(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BizException(ORDER_NOT_FOUND);
        }
        // 幂等校验：只有待支付状态才能修改（防止支付宝重复回调）
        if (!Objects.equals(order.getStatus(), PENDING_PAYMENT)) {
            return; // 已经是已支付状态了，直接返回，不报错
        }

        // 1. 修改订单状态
        order.setStatus(PAID);
        order.setPayTime(LocalDateTime.now());
        orderMapper.updateById(order);

        // 2. 添加 payment 记录
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setPayNo(System.currentTimeMillis() + String.format("%04d", ThreadLocalRandom.current().nextInt(10000)));
        payment.setAmount(order.getPayAmount());
        payment.setPayType(ALIPAY);
        payment.setStatus(PAID);
        payment.setPayTime(LocalDateTime.now());
        paymentMapper.insert(payment);

    }

    /**
     * 查询商家7天销售趋势
     * @param merchantId 商家id
     * @return 返回值
     */
    @Override
    public List<DailySalesVO> getMerchantSalesTrend(Long merchantId) {
        return orderMapper.getMerchantSalesTrend(merchantId);
    }


    /**
     * 订单取消支付
     * @param orderNo 订单编号
     */
    @Override
    @Transactional
    public void cancelPay(Long userId, String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !Objects.equals(order.getUserId(), userId)) {
            throw new BizException(ORDER_NOT_FOUND);
        }
        if (!Objects.equals(order.getStatus(), PENDING_PAYMENT)) {
            throw new BizException(ORDER_STATUS_ERROR);
        }
        order.setStatus(CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        orderMapper.updateById(order);

        List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());

        if (Objects.equals(order.getSource(), SECKILL)) {
            // 秒杀订单：不回滚普通库存，发 MQ 回滚秒杀库存 + Redis
            OrderCancelMessage msg = new OrderCancelMessage();
            msg.setOrderNo(order.getOrderNo());
            msg.setUserId(order.getUserId());
            msg.setItems(items.stream().map(i ->
                    new OrderCancelMessage.Item(i.getSkuId(), i.getQuantity())).toList());
            mqProducerService.send(ORDER_CANCEL_TOPIC, msg);
        } else {
            // 普通订单：回滚普通库存
            for (OrderItem item : items) {
                productService.restoreStock(item.getSkuId(), item.getQuantity());
            }
        }

        UserCoupon userCoupon = userCouponMapper.selectByOrderId(order.getId()); // 需新增 Mapper 方法
        if (userCoupon != null) {
            userCoupon.setStatus(NOT_USED);
            userCoupon.setOrderId(null);
            userCoupon.setUseTime(null);
            userCouponMapper.updateById(userCoupon);
        }
    }

    /**
     * 用户订单分页查询
     * @param userOrderPageQueryDTO 分页查询参数
     * @return 返回值
     */
    @Override
    public PageResult<UserOrderPageResultVO> userPageQuery(UserOrderPageQueryDTO userOrderPageQueryDTO) {
        //先查询订单主表
        Page<UserOrderPageResultVO> page = new Page<>(userOrderPageQueryDTO.getPage(), userOrderPageQueryDTO.getPageSize());
        Page<UserOrderPageResultVO> result = orderMapper.pageQuery(page,userOrderPageQueryDTO);
        long total = result.getTotal();
        //再查询每个订单的具体项
        List<Long> orderIds = result.getRecords().stream().map(UserOrderPageResultVO::getId).toList();
        List<OrderItemVO> itemVOList = null;
        if (!orderIds.isEmpty()) {
            itemVOList = orderItemMapper.selectByOrderIds(orderIds);
        }

        //按订单id分组
        Map<Long,List<OrderItemVO>> group = null;
        if (itemVOList != null) {
            group = itemVOList.stream().collect(Collectors.groupingBy(OrderItemVO::getOrderId));
        }
        //给每组进行组装
        List<UserOrderPageResultVO> records = result.getRecords();
        for (UserOrderPageResultVO record : records) {
            List<OrderItemVO> items = null;
            if (group != null && !group.isEmpty()) {
                items = group.get(record.getId());
            }
            if(items == null){
                items = new ArrayList<>();
            }
            record.setItems(items);
        }
        return new PageResult<>(total,records);
    }

    /**
     * 用户查询订单详情
     * @param orderNo 订单号
     * @return 返回值
     */
    @Override
    public UserOrderDetailVO userDetailQuery(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null || !order.getUserId().equals(StpUtil.getLoginIdAsLong())){
            throw new BizException(ORDER_NOT_FOUND);
        }
        String merchantName = merchantProfileService.getMerchantNameByMerchantId(order.getMerchantId());
        if(merchantName == null){
            merchantName = MERCHANT_NOT_FOUND;
        }
        UserOrderDetailVO detailVO = new UserOrderDetailVO();
        BeanUtils.copyProperties(order,detailVO);
        detailVO.setMerchantName(merchantName);
        detailVO.setDiscountAmount(order.getTotalAmount().subtract(order.getPayAmount()));

        //查询物流信息 (如果订单状态是已发货或已完成)
        if (Objects.equals(order.getStatus(), ORDER_SHIPPED) || Objects.equals(order.getStatus(), COMPLETED)) {
            Logistics logistics = logisticsMapper.selectByOrderId(order.getId());
            if (logistics != null) {
                UserLogisticsVO logisticsVO = new UserLogisticsVO();
                BeanUtils.copyProperties(logistics, logisticsVO);
                detailVO.setLogistics(logisticsVO);
            }
        }

        //查优惠券使用情况(如果实付金额比总金额少就是用了优惠券)
        if (detailVO.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
            UserCoupon userCoupon = userCouponMapper.selectByOrderId(order.getId());
            if (userCoupon != null) {
                Coupon coupon = couponMapper.selectById(userCoupon.getCouponId());
                if (coupon != null) {
                    OrderCouponVO couponVO = new OrderCouponVO();
                    couponVO.setCouponName(coupon.getName());
                    couponVO.setType(coupon.getType());
                    couponVO.setValue(coupon.getValue());
                    detailVO.setCouponInfo(couponVO);
                }
            }
        }

        //查找订单项
        List<OrderItem> itemList = orderItemMapper.selectByOrderId(order.getId());
        List<OrderItemVO> itemVOList = new ArrayList<>();
        for (OrderItem item : itemList) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(item,orderItemVO);
            itemVOList.add(orderItemVO);
        }
        detailVO.setItems(itemVOList);


        return detailVO;
    }

    /**
     * 商家分页查询订单
     * @param merchantOrderPageQueryDTO 查询条件
     * @return 返回值
     */
    @Override
    public PageResult<MerchantOrderPageResultVO> merchantPageQuery(MerchantOrderPageQueryDTO merchantOrderPageQueryDTO) {
        Page<MerchantOrderPageResultVO> page = new Page<>(merchantOrderPageQueryDTO.getPage(), merchantOrderPageQueryDTO.getPageSize());
        Long merchantId = StpUtil.getLoginIdAsLong();
        Page<MerchantOrderPageResultVO> result = orderMapper.MerchantPageQuery(page,merchantOrderPageQueryDTO,merchantId);
        long total = result.getTotal();

        //拼装订单项
        List<Long> orderIds = result.getRecords().stream().map(MerchantOrderPageResultVO::getId).toList();
        List<OrderItemVO> itemVOList = null;
        if (!orderIds.isEmpty()) {
            itemVOList = orderItemMapper.selectByOrderIds(orderIds);
        }

        //分组
        Map<Long,List<OrderItemVO>> group = null;
        if (itemVOList != null) {
            group = itemVOList.stream().collect(Collectors.groupingBy(OrderItemVO::getOrderId));
        }
        //每组进行操作
        List<MerchantOrderPageResultVO> records = result.getRecords();
        for (MerchantOrderPageResultVO record : records) {
            List<OrderItemVO> items = null;
            if (group != null && !group.isEmpty()) {
                items = group.get(record.getId());
            }
            if(items == null){
                items = new ArrayList<>();
            }
            record.setItems(items);
        }
        return new PageResult<>(total,records);
    }


    /**
     * 商家查询订单详情
     * @param orderNo 订单号
     * @return 返回值
     */
    @Override
    public MerchantOrderDetailVO merchantDetailQuery(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null || !order.getMerchantId().equals(StpUtil.getLoginIdAsLong())){
            throw new BizException(ORDER_NOT_FOUND);
        }
        String buyerName = userProfileService.getUserNicknameByUserId(order.getUserId());
        if(buyerName == null){
            buyerName = USER_NOT_FOUND;
        }
        MerchantOrderDetailVO detailVO = new MerchantOrderDetailVO();
        BeanUtils.copyProperties(order,detailVO);
        detailVO.setBuyerUsername(buyerName);
        detailVO.setDiscountAmount(order.getTotalAmount().subtract(order.getPayAmount()));

        //查物流
        if (Objects.equals(order.getStatus(), ORDER_SHIPPED) || Objects.equals(order.getStatus(), COMPLETED)) {
            Logistics logistics = logisticsMapper.selectByOrderId(order.getId());
            if (logistics != null) {
                UserLogisticsVO logisticsVO = new UserLogisticsVO();
                BeanUtils.copyProperties(logistics, logisticsVO);
                detailVO.setLogistics(logisticsVO);
            }
        }

        //查优惠券使用情况
        if (detailVO.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
            UserCoupon userCoupon = userCouponMapper.selectByOrderId(order.getId());
            if (userCoupon != null) {
                Coupon coupon = couponMapper.selectById(userCoupon.getCouponId());
                if (coupon != null) {
                    OrderCouponVO couponVO = new OrderCouponVO();
                    couponVO.setCouponName(coupon.getName());
                    couponVO.setType(coupon.getType());
                    couponVO.setValue(coupon.getValue());
                    detailVO.setCouponInfo(couponVO);
                }
            }
        }

        //查找订单项
        List<OrderItem> itemList = orderItemMapper.selectByOrderId(order.getId());
        List<OrderItemVO> itemVOList = new ArrayList<>();
        for (OrderItem item : itemList) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(item,orderItemVO);
            itemVOList.add(orderItemVO);
        }
        detailVO.setItems(itemVOList);
        return detailVO;
    }

    /**
     * 商家发货
     * @param orderNo 订单号
     */
    @Override
    @Transactional
    public void ship(String orderNo,String company) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getMerchantId().equals(StpUtil.getLoginIdAsLong())) {
            throw new BizException(ORDER_NOT_FOUND);
        }
        if (order.getStatus() != PAID) {
            throw new BizException(ORDER_STATUS_ERROR);
        }
        Logistics exsitedlogistics = logisticsMapper.selectByOrderId(order.getId());
        if(exsitedlogistics != null){
            throw new BizException(LOGISTICS_ALREADY_EXIST);
        }
        Logistics logistics = new Logistics();
        logistics.setOrderId(order.getId());
        logistics.setCompany(DEFAULT_LOGISTICS_COMPANY);
        if(company != null){
            logistics.setCompany(company);
        }
        logistics.setTrackingNo(orderNo + System.currentTimeMillis());
        logistics.setStatus(LOGISTICS_SHIPPED);
        logisticsMapper.insert(logistics);

        order.setStatus(ORDER_SHIPPED);
        orderMapper.updateById(order);
    }

    /**
     * 管理员分页查询订单
     * @param adminOrderPageQueryDTO 分页参数
     * @return 返回值
     */
    @Override
    public PageResult<AdminOrderPageResultVO> adminPageQuery(AdminOrderPageQueryDTO adminOrderPageQueryDTO) {
        Page<AdminOrderPageResultVO> page = new Page<>(adminOrderPageQueryDTO.getPage(),adminOrderPageQueryDTO.getPageSize());
        Page<AdminOrderPageResultVO> result = orderMapper.AdminPageQuery(page,adminOrderPageQueryDTO);
        long total = result.getTotal();
        List<Long> orderIds = result.getRecords().stream().map(AdminOrderPageResultVO::getId).toList();
        List<OrderItemVO> itemVOList = null;
        if (!orderIds.isEmpty()) {
            itemVOList = orderItemMapper.selectByOrderIds(orderIds);
        }
        Map<Long,List<OrderItemVO>> group = null;
        if (itemVOList != null) {
            group = itemVOList.stream().collect(Collectors.groupingBy(OrderItemVO::getOrderId));
        }
        List<AdminOrderPageResultVO> records = result.getRecords();
        for (AdminOrderPageResultVO record : records) {
            List<OrderItemVO> items = null;
            if (group != null && !group.isEmpty()) {
                items = group.get(record.getId());
            }
            if(items == null){
                items = new ArrayList<>();
            }
            record.setItems(items);
        }
        return new PageResult<>(total,records);
    }

    /**
     * 订单超时关闭逻辑
     * @param orderNo 订单号
     */
    @Override
    @Transactional
    public void closeTimeoutOrder(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) return;
        if (!Objects.equals(order.getStatus(), PENDING_PAYMENT)) return;

        order.setStatus(CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        orderMapper.updateById(order);

        List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());

        if (Objects.equals(order.getSource(), SECKILL)) {
            // 秒杀订单：不回滚普通库存（秒杀时没扣普通库存），发 MQ 通知 marketing 回滚秒杀库存 + Redis
            OrderCancelMessage msg = new OrderCancelMessage();
            msg.setOrderNo(order.getOrderNo());
            msg.setUserId(order.getUserId());
            msg.setItems(items.stream().map(i ->
                    new OrderCancelMessage.Item(i.getSkuId(), i.getQuantity())).toList());
            mqProducerService.send(ORDER_CANCEL_TOPIC, msg);
        } else {
            // 普通订单：回滚普通库存
            for (OrderItem item : items) {
                productService.restoreStock(item.getSkuId(), item.getQuantity());
            }
        }

        // 退还优惠券
        UserCoupon userCoupon = userCouponMapper.selectByOrderId(order.getId());
        if (userCoupon != null) {
            userCoupon.setStatus(NOT_USED);
            userCoupon.setOrderId(null);
            userCoupon.setUseTime(null);
            userCouponMapper.updateById(userCoupon);
        }

        log.info("[订单超时] 订单 {} 关闭成功，来源={}", orderNo, order.getSource());
    }

    /**
     * 新增订单
     * @param order 订单
     */
    @Override
    public void insertOrder(Order order) {
        orderMapper.insert(order);
    }

    /**
     * 新增订单明细
     * @param item 订单项
     */
    @Override
    public void insertOrderItem(OrderItem item){
        orderItemMapper.insert(item);
    }

    /**
     * 用户查询物流信息
     * @param userId 用户id
     * @param orderNo 订单号
     * @return 返回值
     */
    @Override
    public UserLogisticsVO logisticsQuery(Long userId, String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null || !order.getUserId().equals(userId)){
            throw new BizException(ORDER_NOT_FOUND);
        }
        Logistics logistics = logisticsMapper.selectByOrderId(order.getId());
        if(logistics == null){
            throw new BizException(ORDER_NOT_FOUND);
        }
        UserLogisticsVO logisticsVO = new UserLogisticsVO();
        BeanUtils.copyProperties(logistics,logisticsVO);
        logisticsVO.setShipTime(logistics.getCreateTime());
        return logisticsVO;
    }

    /**
     * 用户确认收货
     * @param userId 用户id
     * @param orderNo 订单号
     */
    @Override
    @Transactional
    public void receive(Long userId, String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null || !order.getUserId().equals(userId)){
            throw new BizException(ORDER_NOT_FOUND);
        }
        Logistics logistics = logisticsMapper.selectByOrderId(order.getId());
        if(logistics == null){
            throw new BizException(ORDER_NOT_FOUND);
        }
        if(order.getStatus() != ORDER_SHIPPED || logistics.getStatus() != LOGISTICS_SHIPPED){
            throw new BizException(ORDER_STATUS_ERROR);
        }
        logistics.setStatus(SIGNED);
        logisticsMapper.updateById(logistics);
        order.setStatus(COMPLETED);
        order.setFinishTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    /**
     * 管理员查询订单详情
     * @param orderNo 订单号
     * @return 返回值
     */
    @Override
    public AdminOrderDetailVO adminDetailQuery(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            throw new BizException(ORDER_NOT_FOUND);
        }
        String buyerName = userProfileService.getUserNicknameByUserId(order.getUserId());
        if(buyerName == null){
            buyerName = USER_NOT_FOUND;
        }
        String merchantName = merchantProfileService.getMerchantNameByMerchantId(order.getMerchantId());
        if(merchantName == null){
            merchantName = MERCHANT_NOT_FOUND;
        }
        AdminOrderDetailVO detailVO = new AdminOrderDetailVO();
        BeanUtils.copyProperties(order,detailVO);
        detailVO.setBuyerUsername(buyerName);
        detailVO.setMerchantName(merchantName);
        detailVO.setDiscountAmount(order.getTotalAmount().subtract(order.getPayAmount()));

        //查物流
        if (Objects.equals(order.getStatus(), ORDER_SHIPPED) || Objects.equals(order.getStatus(), COMPLETED)) {
            Logistics logistics = logisticsMapper.selectByOrderId(order.getId());
            if (logistics != null) {
                UserLogisticsVO logisticsVO = new UserLogisticsVO();
                BeanUtils.copyProperties(logistics, logisticsVO);
                detailVO.setLogistics(logisticsVO);
            }
        }
        //查优惠券使用情况
        if (detailVO.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
            UserCoupon userCoupon = userCouponMapper.selectByOrderId(order.getId());
            if (userCoupon != null) {
                Coupon coupon = couponMapper.selectById(userCoupon.getCouponId());
                if (coupon != null) {
                    OrderCouponVO couponVO = new OrderCouponVO();
                    couponVO.setCouponName(coupon.getName());
                    couponVO.setType(coupon.getType());
                    couponVO.setValue(coupon.getValue());
                    detailVO.setCouponInfo(couponVO);
                }
            }
        }

        //查找订单项
        List<OrderItem> itemList = orderItemMapper.selectByOrderId(order.getId());
        List<OrderItemVO> itemVOList = new ArrayList<>();
        for (OrderItem item : itemList) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(item,orderItemVO);
            itemVOList.add(orderItemVO);
        }
        detailVO.setItems(itemVOList);
        return detailVO;
    }

    /**
     * 获取商家今日营业额
     * @param merchantId 商家id
     * @return 返回值
     */
    @Override
    public BigDecimal getTodaySales(Long merchantId) {
        return orderMapper.selectTodaySales(merchantId);
    }

    /**
     * 获取商家今日订单数
     * @param merchantId 商家id
     * @return 返回值
     */
    @Override
    public Long getTodayOrderCount(Long merchantId) {
        return orderMapper.selectTodayOrderCount(merchantId);
    }

    /**
     * 获取商家累计销售额
     * @param merchantId 商家id
     * @return 返回值
     */
    @Override
    public BigDecimal getTotalSales(Long merchantId) {
        return orderMapper.selectTodaySales(merchantId);
    }

    /**
     * 获取商家待发货数量
     * @param merchantId 商家id
     * @return 返回值
     */
    @Override
    public Long getPendingShipCount(Long merchantId) {
        return orderMapper.selectPendingShipCount(merchantId);
    }

    /**
     * 获取今日平台总销售额
     * @return 返回值
     */
    @Override
    public BigDecimal getTodayPlatformSales() {
        return orderMapper.selectTodayPlatformSales();
    }

    /**
     * 获取今日平台总订单数
     * @return 返回值
     */
    @Override
    public Long getTodayPlatformOrders() {
        return orderMapper.selectTodayPlatformOrderCount();
    }

    /**
     * 获取近7日销售趋势
     * @return 返回值
     */
    @Override
    public List<DailySalesVO> getSalesTrend() {
        return orderMapper.getSalesTrend();
    }

    /**
     * 核心下单逻辑
     * @param userId 用户id
     * @param addressId 地址id
     * @param items 待下单商品
     * @param clearCartItemIds 需要清理的购物车项id
     * @return 订单号列表
     */
    @Transactional
    protected UserOrderCreateVO doCreateOrder(Long userId, Long addressId, List<CartItemListBO> items, List<Long> clearCartItemIds, Long userCouponId) {
        //1.校验地址属于登录用户
        UserAddress userAddress = userAddressService.getById(addressId);
        if (userAddress == null || !Objects.equals(userAddress.getUserId(), userId)) {
            throw new BizException(ADDRESS_NOT_FOUND);
        }

        //2.拼装地址字符串
        String receiverAddress = userAddress.getProvince() + userAddress.getCity() + userAddress.getDistrict() + userAddress.getDetail();

        //3.提前校验优惠券（如果传了的话）
        UserCoupon userCoupon = null;
        Coupon coupon = null;
        if (userCouponId != null) {
            userCoupon = userCouponMapper.selectByIdAndUserId(userCouponId, userId);
            if (userCoupon == null) throw new BizException(COUPON_NOT_FOUND);
            if (userCoupon.getStatus() != NOT_USED) throw new BizException(COUPON_NOT_USABLE);
            if (userCoupon.getExpireTime().isBefore(LocalDateTime.now())) throw new BizException(COUPON_EXPIRE_OUT);

            coupon = couponMapper.selectById(userCoupon.getCouponId());
            if (coupon == null) throw new BizException(COUPON_NOT_FOUND);
        }

        //4.按商家id分组
        Map<Long, List<CartItemListBO>> group = items.stream().collect(Collectors.groupingBy(CartItemListBO::getMerchantId));
        List<String> orderNos = new ArrayList<>();
        List<BigDecimal> totalAmountList = new ArrayList<>();
        boolean couponUsed = false; // 标记券是否被使用

        //5.遍历每个商家的商品组
        for (Map.Entry<Long, List<CartItemListBO>> entry : group.entrySet()) {
            Long merchantId = entry.getKey();
            List<CartItemListBO> merchantItems = entry.getValue();

            // 生成订单号
            String orderNo = System.currentTimeMillis() + String.format("%04d", ThreadLocalRandom.current().nextInt(10000));

            // 计算当前商家的总金额
            BigDecimal totalPrice = BigDecimal.ZERO;
            for (CartItemListBO boItem : merchantItems) {
                totalPrice = totalPrice.add(boItem.getPrice().multiply(BigDecimal.valueOf(boItem.getQuantity())));
            }

            BigDecimal discount = BigDecimal.ZERO;

            //6.如果传了券，且正好是当前商家的
            if (userCoupon != null && Objects.equals(coupon.getMerchantId(), merchantId)) {
                if (totalPrice.compareTo(coupon.getMinAmount()) < 0) {
                    throw new BizException(COUPON_NOT_USABLE);
                }
                if (Objects.equals(coupon.getType(), AMOUNT_OFF_COUPON)) {
                    discount = coupon.getValue();
                } else {
                    // 折扣券计算，保留两位小数
                    discount = totalPrice.multiply(BigDecimal.ONE.subtract(coupon.getValue())).setScale(2, RoundingMode.HALF_UP);
                }
                couponUsed = true;
            }

            // 计算实付金额
            BigDecimal payAmount = totalPrice.subtract(discount);
            if (payAmount.compareTo(BigDecimal.ZERO) < 0) {
                payAmount = BigDecimal.ZERO; // 防止负数
            }

            totalAmountList.add(payAmount);

            // 插入订单表
            Order order = new Order();
            order.setOrderNo(orderNo);
            order.setUserId(userId);
            order.setMerchantId(merchantId);
            order.setTotalAmount(totalPrice);
            order.setPayAmount(payAmount);
            order.setStatus(PENDING_PAYMENT); // 待支付
            order.setReceiverName(userAddress.getReceiverName());
            order.setReceiverPhone(userAddress.getReceiverPhone());
            order.setReceiverAddress(receiverAddress);
            order.setSource(NORMAL);
            orderMapper.insert(order);

            // 扣除库存并插入订单明细表
            for (CartItemListBO item : merchantItems) {
                int affected = productService.deductStock(item.getSkuId(), item.getQuantity());
                if (affected == 0) {
                    throw new BizException(item.getProductName() + OUT_OF_STOCK);
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setProductId(item.getProductId());
                orderItem.setSkuId(item.getSkuId());
                orderItem.setProductName(item.getProductName());
                orderItem.setSkuSpecs(item.getSkuSpecs());
                if(item.getSkuImage() != null){
                    orderItem.setImage(item.getSkuImage());
                }else{
                    orderItem.setImage(item.getMainImage());
                }
                orderItem.setPrice(item.getPrice());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                orderItemMapper.insert(orderItem);
            }

            orderNos.add(orderNo);

            //7.如果使用了券，标记已使用，并绑定 orderId
            if (userCoupon != null && discount.compareTo(BigDecimal.ZERO) > 0) {
                // 防止并发状态下优惠券被重复使用，使用乐观锁扣减优惠券
                int affected = userCouponMapper.useCoupon(userCoupon.getId(), order.getId());
                if (affected == 0) {
                    throw new BizException(COUPON_NOT_USABLE);
                }
                userCoupon.setStatus(USED);
                userCoupon.setOrderId(order.getId());
                userCoupon.setUseTime(LocalDateTime.now());
            }
        }

        //8.用户传了券，但购物车里没有该商家的商品
        if (userCouponId != null && !couponUsed) {
            throw new BizException(COUPON_NOT_USE);
        }

        //循环外发送消息队列
        for (String orderNo : orderNos) {
            OrderTimeoutMessage message = new OrderTimeoutMessage();
            message.setOrderNo(orderNo);
            message.setSendTime(System.currentTimeMillis());
            mqProducerService.sendDelay(ORDER_TIMEOUT_TOPIC, message, THIRTY_MINUTE_DELAY);
        }

        //9.清空购物车
        if (clearCartItemIds != null && !clearCartItemIds.isEmpty()) {
            cartItemMapper.deleteBatchIds(clearCartItemIds);
        }

        //10.返回订单号和金额
        BigDecimal totalAmount = totalAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return new UserOrderCreateVO(orderNos, totalAmount);
    }
}
