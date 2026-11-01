package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.config.AlipayConfig;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.OrderItemMapper;
import cn.hisouten.mall.mapper.OrderMapper;
import cn.hisouten.mall.mq.message.ProductSalesMessage;
import cn.hisouten.mall.mq.producer.MqProducerService;
import cn.hisouten.mall.pojo.entity.Order;
import cn.hisouten.mall.pojo.entity.OrderItem;
import cn.hisouten.mall.service.AlipayService;
import cn.hisouten.mall.service.OrderService;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.PayConstant.*;
import static cn.hisouten.mall.constant.StatusConstant.PAID;
import static cn.hisouten.mall.constant.StatusConstant.PENDING_PAYMENT;
import static cn.hisouten.mall.mq.constant.MqConstant.PRODUCT_ADD_SALES_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayServiceImpl implements AlipayService {

    private final OrderItemMapper orderItemMapper;

    private final AlipayClient alipayClient;
    private final AlipayConfig alipayConfig;
    private final OrderMapper orderMapper;
    private final OrderService orderService;

    private final MqProducerService mqProducerService;

    /**
     * 生成支付宝支付表单
     * @param orderNo 订单号
     * @return 支付表单
     */
    @Override
    public String createPayForm(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || order.getStatus() != PENDING_PAYMENT) {
            throw new BizException(ORDER_NOT_FOUND);
        }
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(alipayConfig.getReturnUrl());
        request.setNotifyUrl(alipayConfig.getNotifyUrl());

        String bizContent = String.format("{" +
                "\"out_trade_no\":\"%s\"," +
                "\"total_amount\":\"%s\"," +
                "\"subject\":\"HisoutenMall-订单支付\"," +
                "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"" +
                "}", orderNo, order.getPayAmount());
        request.setBizContent(bizContent);

        try {
            return alipayClient.pageExecute(request).getBody();
        } catch (Exception e) {
            log.error("生成支付宝支付表单失败", e);
            throw new BizException(PAYMENT_CALL_FAILED);
        }
    }

    /**
     * 处理支付宝回调
     * @param params 支付宝回调参数
     */
    @Override
    public void handleNotify(Map<String, String> params) {
        String orderNo = params.get(OUT_TRADE_NO);
        String tradeStatus = params.get(TRADE_STATUS);
        String totalAmount = params.get(TOTAL_AMOUNT);
        Order order = orderMapper.selectByOrderNo(orderNo);

        try {
            // 1. 验签
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    params, alipayConfig.getAlipayPublicKey(), "utf-8", "RSA2");
            if (!signVerified) {
                throw new BizException(SIGNATURE_VERIFY_FAILED);
            }

            if (TRADE_SUCCESS.equals(tradeStatus) || TRADE_FINISHED.equals(tradeStatus)) {
                if (order == null || order.getPayAmount().compareTo(new BigDecimal(totalAmount)) != 0) {
                    throw new BizException(AMOUNT_VERIFY_FAILED);
                }
                if(order.getStatus() == PAID){
                    return;
                }
                // 调用订单模块原有的支付成功逻辑（保证幂等）
                orderService.paySuccess(orderNo);
            }
        } catch (Exception e) {
            log.error("处理支付宝回调异常", e);
            throw new BizException(HANDLE_NOTIFY_FAILED);
        }


        // 查出这个订单对应的所有商品明细
        List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getId());

        for (OrderItem item : orderItems) {
            // 组装销量累加消息
            ProductSalesMessage msg = new ProductSalesMessage(item.getProductId(), item.getQuantity());

            // 发送给 RocketMQ
            mqProducerService.send(PRODUCT_ADD_SALES_TOPIC, msg);
        }
    }
}