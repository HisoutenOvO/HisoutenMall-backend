package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.dto.MerchantSeckillActivityAddDTO;

public interface SeckillService {

    /**
     * 商家端新增秒杀活动
     * @param merchantSeckillActivityAddDTO 活动参数
     */
    void createActivity(Long merchantId, MerchantSeckillActivityAddDTO merchantSeckillActivityAddDTO);
}
