package cn.hisouten.mall.service;

import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.activity.MerchantSeckillActivityPageQueryDTO;
import cn.hisouten.mall.pojo.dto.activity.MerchantSeckillActivitySaveDTO;
import cn.hisouten.mall.pojo.dto.product.MerchantSeckillProductAddDTO;
import cn.hisouten.mall.pojo.dto.product.MerchantSeckillProductUpdateDTO;
import cn.hisouten.mall.pojo.vo.activity.MerchantSeckillActivityDetailVO;
import cn.hisouten.mall.pojo.vo.activity.MerchantSeckillActivityPageResultVO;
import cn.hisouten.mall.pojo.vo.activity.UserSeckillActivityListVO;
import cn.hisouten.mall.pojo.vo.activity.UserSeckillActivityDetailVO;

import java.util.List;

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
    MerchantSeckillActivityDetailVO merchantDetailQuery(Long merchantId, Long activityId);

    /**
     * 商家删除秒杀活动
     * @param merchantId 商家id
     * @param activityId 活动id
     */
    void delete(Long merchantId, Long activityId);

    /**
     * 商家端批量新增秒杀商品
     * @param merchantId 商家Id
     * @param addDTO 商品参数
     */
    void addProduct(Long merchantId, MerchantSeckillProductAddDTO addDTO);

    /**
     * 商家端修改秒杀商品
     * @param merchantId 商家id
     * @param productId 秒杀商品id
     * @param updateDTO 修改参数
     */
    void updateProduct(Long merchantId, Long productId, MerchantSeckillProductUpdateDTO updateDTO);

    /**
     * 商家端删除秒杀商品
     * @param merchantId 商家id
     * @param productId 商品id
     */
    void deleteProduct(Long merchantId, Long productId);

    /**
     * 用户端列表查询秒杀活动
     * @return 返回值
     */
    List<UserSeckillActivityListVO> listQuery();

    /**
     * 用户端查询秒杀活动详情
     * @param activityId 活动id
     * @return 返回值
     */
    UserSeckillActivityDetailVO userDetailQuery(Long activityId);

    /**
     * 用户查询秒杀活动详情回源方法
     * @param activityId 活动id
     * @return 返回值
     */
    UserSeckillActivityDetailVO loadDetailFromDB(Long activityId);
}
