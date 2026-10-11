package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.SeckillActivityMapper;
import cn.hisouten.mall.mapper.SeckillProductMapper;
import cn.hisouten.mall.mq.dto.OrderTimeoutMessage;
import cn.hisouten.mall.mq.dto.SeckillOrderMessage;
import cn.hisouten.mall.mq.producer.MqProducerService;
import cn.hisouten.mall.pojo.dto.SeckillExecuteDTO;
import cn.hisouten.mall.pojo.entity.*;
import cn.hisouten.mall.service.OrderService;
import cn.hisouten.mall.service.ProductService;
import cn.hisouten.mall.service.SeckillOrderService;
import cn.hisouten.mall.user.pojo.entity.UserAddress;
import cn.hisouten.mall.user.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.RedisConstant.*;
import static cn.hisouten.mall.constant.SeckillConstant.*;
import static cn.hisouten.mall.constant.StatusConstant.PENDING_PAYMENT;
import static cn.hisouten.mall.mq.constant.MqConstant.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SeckillOrderServiceImpl implements SeckillOrderService {
    private final SeckillActivityMapper seckillActivityMapper;
    private final SeckillProductMapper seckillProductMapper;

    private final ProductService productService;
    private final UserAddressService userAddressService;
    private final OrderService orderService;

    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;
    private final MqProducerService mqProducerService;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT; //定义脚本类
    //在静态代码块预加载脚本文件
    static{
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua")); //把文件加载进类
        SECKILL_SCRIPT.setResultType(long.class); //定义返回值
    }


    /**
     * 用户秒杀商品
     * @param userId 用户id
     * @param seckillExecuteDTO 商品参数
     * @return 返回下单成功
     */
    @Override
    public String seckillProduct(Long userId, SeckillExecuteDTO seckillExecuteDTO) {
        //1. 先获取Redisson锁对象
        String key = LOCK_SECKILL_ORDER_PREFIX + userId + ":" +seckillExecuteDTO.getSkuId();
        RLock lock = redissonClient.getLock(key);
        boolean isLock;
        try {
            isLock = lock.tryLock(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // 恢复中断状态
            Thread.currentThread().interrupt();
            throw new BizException(SECKILL_HAS_INTERRUPT);
        }
        //判断锁是否成功
        if(!isLock){
            throw new BizException(DO_NOT_CLICK_REPEATEDLY);
        }
        try{
            //2. 判断活动和商品是否有效
            SeckillActivity seckillActivity = seckillActivityMapper.selectById(seckillExecuteDTO.getActivityId());
            if(!Objects.equals(seckillActivity.getStatus(), RUNNING)){
                throw new BizException(ACTIVITY_INVALID);
            }
            SeckillProduct seckillProduct = seckillProductMapper.selectBySkuId(seckillExecuteDTO.getSkuId());
            if(seckillProduct == null){
                throw new BizException(PRODUCT_NOT_FOUND);
            }
            //查询默认地址
            UserAddress userAddress = userAddressService.getDefaultByUserId(userId);
            if (userAddress == null) {
                throw new BizException(SET_DEFAULT_ADDRESS_FIRST);
            }

            //3.执行Lua脚本
            List<String> keys = new ArrayList<>();
            //规定KEYS[1]和KEYS[2]
            keys.add(SECKILL_STOCK + seckillExecuteDTO.getSkuId());
            keys.add(SECKILL_USER + seckillExecuteDTO.getSkuId());
            long result = stringRedisTemplate.execute(
                    SECKILL_SCRIPT,
                    keys,
                    userId.toString(),
                    "1",
                    seckillProduct.getLimitPerUser().toString()
            );
            //判断返回值
            if (result == 1) throw new BizException(OUT_OF_STOCK);
            if (result == 2) throw new BizException(DO_NOT_ORDER_REPEATEDLY);
        }finally {
            lock.unlock();
        }
        //4.发送创建订单消息
        SeckillOrderMessage msg = new SeckillOrderMessage();
        msg.setUserId(userId);
        msg.setSkuId(seckillExecuteDTO.getSkuId());
        msg.setActivityId(seckillExecuteDTO.getActivityId());
        msg.setSendTime(System.currentTimeMillis());
        mqProducerService.send(SECKILL_ORDER_TOPIC, msg);

        //返回成功
        return ORDER_COMPLETED;
    }

    /**
     * 创建秒杀订单
     * @param message 消息体
     */
    @Override
    @Transactional
    public void createSeckillOrder(SeckillOrderMessage message) {
        Long userId = message.getUserId();
        Long activityId = message.getActivityId();
        Long skuId = message.getSkuId();

        //查询商品和sku
        // 活动不存在
        SeckillActivity seckillActivity = seckillActivityMapper.selectById(activityId);
        if (seckillActivity == null || !Objects.equals(seckillActivity.getStatus(), RUNNING)) {
            throw new BizException(ACTIVITY_INVALID);
        }
        LocalDateTime now = LocalDateTime.now();
        if (seckillActivity.getStartTime().isAfter(now) || seckillActivity.getEndTime().isBefore(now)) {
            throw new BizException(ACTIVITY_INVALID);
        }
        ProductSku productSku = productService.getSkuBySkuId(skuId);
        if (productSku == null) {
            log.error("[秒杀对账] SKU不存在,skuId={}", skuId);
            return;
        }
        Product product = productService.getProductByProductId(productSku.getProductId());
        if(product == null){
            log.error("[秒杀对账] sku对应商品不存在,skuId={}", skuId);
            return;
        }
        //校验商品是否是秒杀商品
        SeckillProduct seckillProduct = seckillProductMapper.selectBySkuId(skuId);
        if (seckillProduct == null || !seckillProduct.getActivityId().equals(message.getActivityId())) {
            throw new BizException(PRODUCT_NOT_FOUND);
        }
        UserAddress userAddress = userAddressService.getDefaultByUserId(userId);
        //乐观锁扣减库存
        int affected = productService.deductStock(skuId, 1);
        if (affected == 0) {
            log.error("[秒杀对账] 数据库库存不足，数据库与缓存数据错位，activityId={}, skuId={}, userId={}",
                    activityId, skuId, userId);
            // 不调 rollbackRedisStock
            // 靠定时任务纠正
            return;
        }

        //创建订单（走已有的订单创建逻辑）
        Order order = new Order();
        order.setOrderNo(System.currentTimeMillis()
                + String.format("%04d", ThreadLocalRandom.current().nextInt(10000)));
        order.setUserId(userId);
        order.setMerchantId(product.getMerchantId());
        order.setTotalAmount(seckillProduct.getSeckillPrice());
        order.setPayAmount(seckillProduct.getSeckillPrice());
        order.setStatus(PENDING_PAYMENT);
        order.setReceiverName(userAddress.getReceiverName());
        order.setReceiverPhone(userAddress.getReceiverPhone());
        order.setReceiverAddress(userAddress.getProvince() + userAddress.getCity() + userAddress.getDistrict() +userAddress.getDetail());
        order.setSource(SECKILL);
        orderService.insertOrder(order);

        //创建订单明细
        OrderItem item = new OrderItem();
        item.setOrderId(order.getId());
        item.setProductId(product.getId());
        item.setSkuId(skuId);
        item.setProductName(product.getName());
        item.setSkuSpecs(productSku.getSpecs());
        item.setPrice(seckillProduct.getSeckillPrice());
        item.setQuantity(1);
        item.setTotalPrice(seckillProduct.getSeckillPrice());
        orderService.insertOrderItem(item);

        //发延时消息（订单超时关闭）
        OrderTimeoutMessage timeoutMsg = new OrderTimeoutMessage();
        timeoutMsg.setOrderNo(order.getOrderNo());
        mqProducerService.sendDelay(ORDER_TIMEOUT_TOPIC, timeoutMsg, THIRTY_MINUTE_DELAY);
    }
}
