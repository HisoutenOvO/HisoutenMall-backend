package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.MerchantSeckillActivityPageQueryDTO;
import cn.hisouten.mall.pojo.dto.MerchantSeckillActivitySaveDTO;
import cn.hisouten.mall.pojo.vo.MerchantSeckillActivityDetailVO;
import cn.hisouten.mall.pojo.vo.MerchantSeckillActivityPageResultVO;

public interface SeckillService {

    /**
     * 商家端新增秒杀活动
     * @param merchantSeckillActivitySaveDTO 活动参数
     */
    void createActivity(Long merchantId, MerchantSeckillActivitySaveDTO merchantSeckillActivitySaveDTO);

    /**
     * 商家端修改秒杀活动
     * @param activityId 活动id
     * @param merchantSeckillActivitySaveDTO 活动参数
     */
    void updateActivity(Long merchantId, Long activityId, MerchantSeckillActivitySaveDTO merchantSeckillActivitySaveDTO);

    /**
     * 商家端分页查询秒杀商品
     * @param merchantId 商家id
     * @param pageQueryDTO 分页参数
     * @return 返回值
     */
    PageResult<MerchantSeckillActivityPageResultVO> pageQueryMerchant(Long merchantId, MerchantSeckillActivityPageQueryDTO pageQueryDTO);

    /**
     * 商家端查询秒杀活动详情
     * @param merchantId 商家id
     * @param activityId 活动id
     * @return 返回值
     */
    MerchantSeckillActivityDetailVO detailQuery(Long merchantId, Long activityId);
}
