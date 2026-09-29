package cn.hisouten.mall.user.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.user.mapper.AuthMapper;
import cn.hisouten.mall.user.mapper.MerchantProfileMapper;
import cn.hisouten.mall.user.pojo.entity.MerchantProfile;
import cn.hisouten.mall.user.pojo.entity.User;
import cn.hisouten.mall.user.pojo.vo.MerchantDetailVO;
import cn.hisouten.mall.user.service.MerchantProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MerchantProfileServiceImpl implements MerchantProfileService {
    private final MerchantProfileMapper merchantProfileMapper;
    private final AuthMapper authMapper;

    /**
     * 根据商家id获取商家名称
     * @param merchantId 商家id
     * @return 商家名称
     */
    @Override
    public String getMerchantNameByMerchantId(Long merchantId){
        return merchantProfileMapper.getMerchantNameByMerchantId(merchantId);
    }

    /**
     * 商家查询店铺详情
     * @param merchantId 商家id
     * @return 返回值
     */
    @Override
    public MerchantDetailVO detailQuery(Long merchantId) {
        User user = authMapper.selectById(merchantId);
        if(user == null){
            throw new BizException(USER_NOT_FOUND);
        }
        MerchantProfile merchantProfile = merchantProfileMapper.selectProfileByMerchantId(merchantId);
        return MerchantDetailVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .shopName(merchantProfile.getShopName())
                .shopLogo(merchantProfile.getShopLogo())
                .shopDescription(merchantProfile.getShopDescription())
                .contactPhone(merchantProfile.getContactPhone())
                .businessLicense(merchantProfile.getBusinessLicense())
                .auditStatus(merchantProfile.getAuditStatus())
                .auditReason(merchantProfile.getAuditReason())
                .build();
    }
}
