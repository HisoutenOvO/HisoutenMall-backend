package cn.hisouten.mall.user.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.user.mapper.AuthMapper;
import cn.hisouten.mall.user.mapper.MerchantProfileMapper;
import cn.hisouten.mall.user.pojo.dto.MerchantUpdateDTO;
import cn.hisouten.mall.user.pojo.dto.PasswordUpdateDTO;
import cn.hisouten.mall.user.pojo.entity.MerchantProfile;
import cn.hisouten.mall.user.pojo.entity.User;
import cn.hisouten.mall.user.pojo.vo.MerchantDetailVO;
import cn.hisouten.mall.user.service.MerchantProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;

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
        if(merchantProfile == null){
            throw new BizException(USER_NOT_FOUND);
        }
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

    /**
     * 商家修改信息
     * @param merchantId 商家id
     * @param merchantUpdateDTO 修改参数
     */
    @Override
    public void updateInfo(Long merchantId, MerchantUpdateDTO merchantUpdateDTO) {
        MerchantProfile merchantProfile = merchantProfileMapper.selectProfileByMerchantId(merchantId);
        if(merchantProfile == null){
            throw new BizException(USER_NOT_FOUND);
        }
        //查询店名和联系电话是否重复
        String existedShopName = merchantProfileMapper.selectExistedShopName(merchantUpdateDTO.getShopName());
        if(existedShopName != null){
            throw new BizException(SHOP_NAME_ALREADY_EXIST);
        }
        String existedContactPhone = merchantProfileMapper.selectExistedContactPhone(merchantUpdateDTO.getContactPhone());
        if(existedContactPhone != null){
            throw new BizException(CONTACT_PHONE_ALREADY_EXIST);
        }
        BeanUtils.copyProperties(merchantUpdateDTO,merchantProfile);
        merchantProfileMapper.updateById(merchantProfile);
    }

    /**
     * 商家修改密码
     * @param merchantId 商家id
     * @param passwordUpdateDTO 修改密码参数
     */
    @Override
    public void updatePwd(Long merchantId, PasswordUpdateDTO passwordUpdateDTO) {
        User user = authMapper.selectById(merchantId);
        if(user == null){
            throw new BizException(USER_NOT_FOUND);
        }
        // 1. 原密码校验
        if (!BCrypt.checkpw(passwordUpdateDTO.getOldPwd(), user.getPassword())) {
            throw new BizException(PASSWORD_ERROR);
        }
        // 2. 新旧不能相同
        if (passwordUpdateDTO.getOldPwd().equals(passwordUpdateDTO.getNewPwd())) {
            throw new BizException(SAME_PASSWORD);
        }
        // 3. 加密更新
        user.setPassword(BCrypt.hashpw(passwordUpdateDTO.getNewPwd()));
        authMapper.updateById(user);
    }
}
