package cn.hisouten.mall.user.service;

public interface MerchantProfileService {

    /**
     * 根据商家id获取商家名称
     * @param merchantId 商家id
     * @return 商家名称
     */
    String getMerchantNameByMerchantId(Long merchantId);
}
