package cn.hisouten.mall.user.service;

import cn.hisouten.mall.user.pojo.vo.MerchantDetailVO;

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
}
