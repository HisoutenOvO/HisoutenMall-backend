package cn.hisouten.mall.user.service;

import cn.hisouten.mall.user.pojo.dto.update.MerchantUpdateDTO;
import cn.hisouten.mall.user.pojo.vo.merchant.MerchantDetailVO;
import cn.hisouten.mall.user.pojo.vo.user.UserMerchantDetailVO;

public interface MerchantProfileService {

    /**
     * 根据商家id获取商家名称
     * @param merchantId 商家id
     * @return 商家名称
     */
    String getMerchantNameByMerchantId(Long merchantId);

    /**
     * 商家查询店铺详情
     * @param merchantId 商家id
     * @return 返回值
     */
    MerchantDetailVO detailQuery(Long merchantId);

    /**
     * 商家修改信息
     * @param merchantId 商家id
     * @param merchantUpdateDTO 修改参数
     */
    void updateInfo(Long merchantId, MerchantUpdateDTO merchantUpdateDTO);


    /**
     * 用户查询商家详情
     * @param userId 用户id
     * @param merchantId 商家id
     * @return 返回值
     */
    UserMerchantDetailVO userDetailQuery(Long userId, Long merchantId);

    /**
     * 用户关注店铺
     * @param userId 用户id
     * @param merchantId 店铺id
     */
    void followMerchant(Long userId, Long merchantId);

    /**
     * 用户取关店铺
     * @param userId 用户id
     * @param merchantId 商家id
     */
    void cancelFollow(Long userId, Long merchantId);
}
