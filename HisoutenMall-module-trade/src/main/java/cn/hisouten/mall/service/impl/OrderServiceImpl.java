package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.*;
import cn.hisouten.mall.pojo.bo.CartItemListBO;
import cn.hisouten.mall.pojo.dto.UserOrderCreateDTO;
import cn.hisouten.mall.pojo.dto.UserOrderPayDTO;
import cn.hisouten.mall.pojo.entity.*;
import cn.hisouten.mall.pojo.vo.UserOrderCreateVO;
import cn.hisouten.mall.service.OrderService;
import cn.hisouten.mall.user.pojo.entity.UserAddress;
import cn.hisouten.mall.user.service.UserAddressService;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final CartItemMapper cartItemMapper;
    private final ProductSkuMapper productSkuMapper;
    private final ProductMapper productMapper;
    private final OrderItemMapper orderItemMapper;
    private final PaymentMapper paymentMapper;

    private final UserAddressService userAddressService;


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
        ProductSku sku = productSkuMapper.selectById(userOrderCreateDTO.getSkuId());
        if (sku == null) {
            throw new BizException(SKU_NOT_FOUND);
        }
        Product product = productMapper.selectById(sku.getProductId());
        if(product == null){
            throw new BizException(PRODUCT_NOT_FOUND);
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
            if (order.getStatus() != PENDING_PAYMENT) {
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
                int affected = productSkuMapper.deductStock(item.getSkuId(), item.getQuantity());
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
                orderItem.setPrice(item.getPrice());               // 快照
                orderItem.setQuantity(item.getQuantity());
                orderItem.setTotalPrice(item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())));
                orderItemMapper.insert(orderItem);
                }
                orderNos.add(orderNo);
            });
        //4. 清空购物车
        if (clearCartItemIds != null && !clearCartItemIds.isEmpty()) {
            cartItemMapper.deleteBatchIds(clearCartItemIds);
        };
        //5. 返回订单号和金额
        BigDecimal totalAmount = totalAmountList.stream().reduce(BigDecimal.ZERO,BigDecimal::add);
        return new UserOrderCreateVO(orderNos,totalAmount);
    }
}
