package cn.hisouten.mall.user.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.service.ProductService;
import cn.hisouten.mall.user.mapper.AuthMapper;
import cn.hisouten.mall.user.mapper.MerchantProfileMapper;
import cn.hisouten.mall.user.pojo.dto.update.MerchantUpdateDTO;
import cn.hisouten.mall.user.pojo.entity.MerchantProfile;
import cn.hisouten.mall.user.pojo.entity.User;
import cn.hisouten.mall.user.pojo.vo.merchant.MerchantDetailVO;
import cn.hisouten.mall.user.pojo.vo.user.UserMerchantDetailVO;
import cn.hisouten.mall.user.service.MerchantProfileService;
import cn.hisouten.mall.util.CacheClientUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.RedisConstant.*;
import static cn.hisouten.mall.constant.StatusConstant.DISABLED;
import static cn.hisouten.mall.constant.StatusConstant.ENABLED;

@Service
@RequiredArgsConstructor
public class MerchantProfileServiceImpl implements MerchantProfileService {
    private final MerchantProfileMapper merchantProfileMapper;
    private final ProductService productService;
    private final AuthMapper authMapper;

    private CacheClientUtils cacheClientUtils;

    /**
     * 根据商家id获取商家名称
     * @param merchantId 商家id
     * @return 商家名称
     */
    @Override
    @Cacheable(cacheNames = CACHE_MERCHANT_NAME_PREFIX, key = "#merchantId")
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
    @CacheEvict(value = CACHE_MERCHANT_NAME_PREFIX, key = "#merchantId")
    public void updateInfo(Long merchantId, MerchantUpdateDTO merchantUpdateDTO) {
        MerchantProfile merchantProfile = merchantProfileMapper.selectProfileByMerchantId(merchantId);
        if(merchantProfile == null){
            throw new BizException(USER_NOT_FOUND);
        }
        //查询店名和联系电话是否重复
        String existedShopName = merchantProfileMapper.selectExistedShopNameExceptSelf(merchantUpdateDTO.getShopName(),merchantProfile.getUserId());
        if(existedShopName != null){
            throw new BizException(SHOP_NAME_ALREADY_EXIST);
        }
        String existedContactPhone = merchantProfileMapper.selectExistedContactPhoneExceptSelf(merchantUpdateDTO.getContactPhone(),merchantProfile.getUserId());
        if(existedContactPhone != null){
            throw new BizException(CONTACT_PHONE_ALREADY_EXIST);
        }
        BeanUtils.copyProperties(merchantUpdateDTO,merchantProfile);
        merchantProfileMapper.updateById(merchantProfile);
    }

    /**
     * 用户查询商家详情
     * @param userId 用户id
     * @param merchantId 商家id
     * @return 返回值
     */
    @Override
    public UserMerchantDetailVO userDetailQuery(Long userId, Long merchantId) {
        MerchantProfile merchantProfile = getProfileByCache(merchantId);
        if(merchantProfile == null){
            throw new BizException(MERCHANT_NOT_FOUND);
        }

        Long productCount;
        Long followerCount;
        int isFollowed = 0;

        productCount = productService.getOnSaleProductCount(merchantId);
        followerCount = merchantProfileMapper.selectFollowerCount(merchantId);
        if(userId != null) isFollowed = merchantProfileMapper.isFollowed(userId,merchantId);

        UserMerchantDetailVO detailVO = new UserMerchantDetailVO();
        detailVO.setMerchantId(merchantProfile.getUserId());
        detailVO.setShopName(merchantProfile.getShopName());
        detailVO.setShopLogo(merchantProfile.getShopLogo());
        detailVO.setShopDescription(merchantProfile.getShopDescription());
        detailVO.setProductCount(productCount);
        detailVO.setFollowerCount(followerCount);
        detailVO.setIsFollowed(isFollowed != 0);
        return detailVO;
    }

    /**
     * 通过防穿透缓存查询店铺基本信息
     * @param merchantId 商家id
     * @return 返回值
     */
    private MerchantProfile getProfileByCache(Long merchantId){
        return cacheClientUtils.queryWithPassThrough(
                CACHE_MERCHANT_DETAIL_PREFIX,
                merchantId,
                MerchantProfile.class,
                this::getFromDB,
                CACHE_MERCHANT_DETAIL_TTL,
                TimeUnit.HOURS
        );
    }

    /**
     * 查询商家信息回源方法
     * @param merchantId 商家id
     * @return 返回值
     */
    private MerchantProfile getFromDB(Long merchantId){
        return merchantProfileMapper.selectById(merchantId);
    }

    /**
     * 用户关注店铺
     * @param userId 用户id
     * @param merchantId 店铺id
     */
    @Override
    public void followMerchant(Long userId, Long merchantId) {
        User merchant = authMapper.getUserByUserId(merchantId);
        if(merchant == null || merchant.getStatus() != ENABLED){
            throw new BizException();
        }
        MerchantProfile merchantProfile = merchantProfileMapper.selectProfileByMerchantId(merchantId);
        if(merchantProfile == null){
            throw new BizException(MERCHANT_NOT_FOUND);
        }
        int isFollowed = merchantProfileMapper.isFollowed(userId,merchantId);

        if(isFollowed == 0){
            merchantProfileMapper.follow(userId,merchantId);
        }else{
            return;
        }
    }

    /**
     * 用户取关店铺
     * @param userId 用户id
     * @param merchantId 商家id
     */
    @Override
    public void cancelFollow(Long userId, Long merchantId) {
        User merchant = authMapper.getUserByUserId(merchantId);
        if(merchant == null || merchant.getStatus() != ENABLED){
            throw new BizException();
        }
        MerchantProfile merchantProfile = merchantProfileMapper.selectProfileByMerchantId(merchantId);
        if(merchantProfile == null){
            throw new BizException(MERCHANT_NOT_FOUND);
        }
        int isFollowed = merchantProfileMapper.isFollowed(userId,merchantId);

        if(isFollowed != 0){
            merchantProfileMapper.cancelFollow(userId,merchantId);
        }else{
            return;
        }
    }


}
