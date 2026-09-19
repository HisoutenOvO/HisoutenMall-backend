package cn.hisouten.mall.user.service.impl;

import cn.hisouten.mall.user.mapper.MerchantProfileMapper;
import cn.hisouten.mall.user.service.MerchantProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantProfileServiceImpl implements MerchantProfileService {
    private final MerchantProfileMapper merchantProfileMapper;

    /**
     * 根据商家id获取商家名称
     * @param merchantId 商家id
     * @return 商家名称
     */
    @Override
    public String getMerchantNameByMerchantId(Long merchantId){
        return merchantProfileMapper.getMerchantNameByMerchantId(merchantId);
    }
}
