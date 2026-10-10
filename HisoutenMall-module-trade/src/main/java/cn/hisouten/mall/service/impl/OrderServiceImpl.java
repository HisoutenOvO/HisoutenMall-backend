package cn.hisouten.mall.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.*;
import cn.hisouten.mall.mq.dto.OrderTimeoutMessage;
import cn.hisouten.mall.mq.producer.MqProducerService;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.bo.CartItemListBO;
import cn.hisouten.mall.pojo.dto.*;
import cn.hisouten.mall.pojo.entity.*;
import cn.hisouten.mall.pojo.vo.*;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.PayMethodConstant.WECHAT;
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

    private final UserAddressService userAddressService;
    private final ProductService productService;
    private final MerchantProfileService merchantProfileService;
    private final UserProfileService userProfileService;
    private final MqProducerService mqProducerService;


    /**
     * 用户从购物车里结算下单
     * @param addressId 地址id
     * @return 返回值
     */
    @Override
    @Transactional
    public UserOrderCreateVO createFromCart(Long userId, Long addressId) {
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
        return doCreateOrder(userId,addressId,cartItemList,clearIds);
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
        return doCreateOrder(userId,userOrderCreateDTO.getAddressId(),List.of(item),null);
    }

    /**
     * 用户支付订单
     * @param userOrderPayDTO 支付订单参数
     */
    @Override
    @Transactional
    public void pay(Long userId, UserOrderPayDTO userOrderPayDTO) {
        for (String orderNo : userOrderPayDTO.getOrderNos()) {
            Order order = orderMapper.selectByOrderNo(orderNo);
            // 校验归属
            if (order == null || !order.getUserId().equals(userId)) {
                throw new BizException(ORDER_NOT_FOUND);
            }
            // 校验状态
            if (!Objects.equals(order.getStatus(), PENDING_PAYMENT)) {
                throw new BizException(ORDER_STATUS_ERROR);
            }
            //修改订单状态
            order.setStatus(PAID);
            order.setPayTime(LocalDateTime.now());
            orderMapper.updateById(order);
            //添加payment
            Payment payment = new Payment();
            payment.setOrderId(order.getId());
            payment.setPayNo(System.currentTimeMillis() + String.format("%04d", ThreadLocalRandom.current().nextInt(10000)));
            payment.setAmount(order.getPayAmount());
            payment.setPayType(WECHAT);
            payment.setStatus(PAID);
            payment.setPayTime(LocalDateTime.now());
            paymentMapper.insert(payment);
        }
    }

    /**
     * 订单取消支付
     * @param orderNo 订单编号
     */
    @Override
    @Transactional
    public void cancelPay(Long userId, String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null || !Objects.equals(order.getUserId(), userId)){
            throw new BizException(ORDER_NOT_FOUND);
        }
        if(order.getStatus() != PENDING_PAYMENT){
            throw new BizException(ORDER_STATUS_ERROR);
        }
        //改订单状态
        order.setStatus(CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        orderMapper.updateById(order);
        //给各订单项回滚库存
        List<OrderItem> itemList = orderItemMapper.selectByOrderId(order.getId());
        for (OrderItem item : itemList) {
            productService.restoreStock(item.getSkuId(),item.getQuantity());
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
        UserOrderDetailVO userOrderDetailVO = new UserOrderDetailVO();
        BeanUtils.copyProperties(order,userOrderDetailVO);
        userOrderDetailVO.setMerchantName(merchantName);

        //查找订单项
        List<OrderItem> itemList = orderItemMapper.selectByOrderId(order.getId());
        List<OrderItemVO> itemVOList = new ArrayList<>();
        for (OrderItem item : itemList) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(item,orderItemVO);
            itemVOList.add(orderItemVO);
        }
        userOrderDetailVO.setItems(itemVOList);
        return userOrderDetailVO;
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
        MerchantOrderDetailVO merchantOrderDetailVO = new MerchantOrderDetailVO();
        BeanUtils.copyProperties(order,merchantOrderDetailVO);
        merchantOrderDetailVO.setBuyerUsername(buyerName);

        //查找订单项
        List<OrderItem> itemList = orderItemMapper.selectByOrderId(order.getId());
        List<OrderItemVO> itemVOList = new ArrayList<>();
        for (OrderItem item : itemList) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(item,orderItemVO);
            itemVOList.add(orderItemVO);
        }
        merchantOrderDetailVO.setItems(itemVOList);
        return merchantOrderDetailVO;
    }

    /**
     * 商家发货
     * @param orderNo 订单号
     */
    @Override
    @Transactional
    public void ship(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getMerchantId().equals(StpUtil.getLoginIdAsLong())) {
            throw new BizException(ORDER_NOT_FOUND);
        }
        if (order.getStatus() != PAID) {
            throw new BizException(ORDER_STATUS_ERROR);
        }
        order.setStatus(COMPLETED);   // 阶段二简化,发货即完成
        order.setFinishTime(LocalDateTime.now());
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
        //订单不存在，跳过
        if (order == null) return;
        // 已支付或已取消，跳过
        if (order.getStatus() != PENDING_PAYMENT) return;
        // 关单
        order.setStatus(CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        orderMapper.updateById(order);
        // 回滚库存
        List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
        for (OrderItem item : items) {
            productService.restoreStock(item.getSkuId(), item.getQuantity());
        }
        log.info("[订单超时] 订单 {} 关闭成功，回滚 {} 个 SKU", orderNo, items.size());
    }

    /**
     * 核心下单逻辑
     * @param userId 用户id
     * @param addressId 地址id
     * @param items 待下单商品
     * @param clearCartItemIds 需要清理的购物车项id
     * @return 订单号列表
     */
    private UserOrderCreateVO doCreateOrder(Long userId, Long addressId, List<CartItemListBO> items, List<Long> clearCartItemIds){
        //1. 校验地址属于登录用户
        UserAddress userAddress = userAddressService.getById(addressId);
        if(userAddress == null || !Objects.equals(userAddress.getUserId(), userId)){
            throw new BizException(ADDRESS_NOT_FOUND);
        }
        //2. 拼装地址字符串
        String receiverAddress = userAddress.getProvince() + userAddress.getCity() + userAddress.getDistrict() +userAddress.getDetail();
        //3. 按商家id分组，分组后对每组进行分别操作
        Map<Long,List<CartItemListBO>> group = items.stream().collect(Collectors.groupingBy(CartItemListBO::getMerchantId));
        List<String> orderNos = new ArrayList<>();
        List<BigDecimal> totalAmountList = new ArrayList<>();
        group.forEach((merchantId,bo)->{
            //生成订单号，用时间戳随机生成
            String orderNo = System.currentTimeMillis()
                + String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
            //计算每组的总金额
            BigDecimal totalPrice = BigDecimal.ZERO;
            for (CartItemListBO boItem : bo) {
                totalPrice = totalPrice.add(
                        boItem.getPrice().multiply(BigDecimal.valueOf(boItem.getQuantity()))
                );
            }
            totalAmountList.add(totalPrice);
            //插入订单表
            Order order = new Order();
            order.setOrderNo(orderNo);
            order.setUserId(userId);
            order.setMerchantId(merchantId);
            order.setTotalAmount(totalPrice);
            order.setPayAmount(totalPrice);   // 阶段二不做优惠，实付是总额
            order.setStatus(PENDING_PAYMENT); // 待支付
            order.setReceiverName(userAddress.getReceiverName());
            order.setReceiverPhone(userAddress.getReceiverPhone());
            order.setReceiverAddress(receiverAddress);
            orderMapper.insert(order);
            //扣除库存并插入订单明细表
            for (CartItemListBO item : bo) {
                int affected = productService.deductStock(item.getSkuId(), item.getQuantity());
                if (affected == 0) {
                    throw new BizException(item.getProductName() + OUT_OF_STOCK);
                }
                // 插明细
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setProductId(item.getProductId());
                orderItem.setSkuId(item.getSkuId());
                orderItem.setProductName(item.getProductName());   // 快照
                orderItem.setSkuSpecs(item.getSkuSpecs());         // 快照
                if(item.getSkuImage() != null){
                    orderItem.setImage(item.getSkuImage());        // 快照
                }else{
                    orderItem.setImage(item.getMainImage());       // 快照
                }
                orderItem.setPrice(item.getPrice());               // 快照
                orderItem.setQuantity(item.getQuantity());
                orderItem.setTotalPrice(item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())));
                orderItemMapper.insert(orderItem);
                }
                orderNos.add(orderNo);
            //发送订单创建消息
            OrderTimeoutMessage message = new OrderTimeoutMessage();
            message.setOrderNo(orderNo);
            message.setSendTime(System.currentTimeMillis());
            mqProducerService.sendDelay(
                    ORDER_TIMEOUT_TOPIC,
                    message,
                    THIRTY_MINUTE_DELAY
            );
            });
        //4. 清空购物车
        if (clearCartItemIds != null && !clearCartItemIds.isEmpty()) {
            cartItemMapper.deleteBatchIds(clearCartItemIds);
        }
        //5. 返回订单号和金额
        BigDecimal totalAmount = totalAmountList.stream().reduce(BigDecimal.ZERO,BigDecimal::add);
        return new UserOrderCreateVO(orderNos,totalAmount);
    }
}
