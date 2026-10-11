package cn.hisouten.mall.service;

import cn.hisouten.mall.mq.dto.SeckillOrderMessage;
import cn.hisouten.mall.pojo.dto.SeckillExecuteDTO;

public interface SeckillOrderService {
    /**
     * 用户秒杀商品
     * @param userId 用户id
     * @param seckillExecuteDTO 商品参数
     * @return 返回下单成功
     */
    String seckillProduct(Long userId, SeckillExecuteDTO seckillExecuteDTO);

    /**
     * 创建秒杀订单
     * @param message 消息体
     */
    void createSeckillOrder(SeckillOrderMessage message);
}
