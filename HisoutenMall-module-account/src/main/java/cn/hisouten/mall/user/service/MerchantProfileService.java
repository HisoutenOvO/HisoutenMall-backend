package cn.hisouten.mall.user.service;

import cn.hisouten.mall.user.pojo.dto.MerchantUpdateDTO;
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

    /**
     * 商家修改信息
     * @param merchantId 商家id
     * @param merchantUpdateDTO 修改参数
     */
    void updateInfo(Long merchantId, MerchantUpdateDTO merchantUpdateDTO);
}
