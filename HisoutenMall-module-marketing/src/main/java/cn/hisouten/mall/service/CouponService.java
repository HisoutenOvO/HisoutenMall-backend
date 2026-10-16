package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.dto.MerchantCouponAddDTO;

public interface CouponService {
    /**
     * 商家新增优惠券
     * @param merchantId 商家id
     * @param addDTO 新增参数
     */
    void addCoupon(Long merchantId, MerchantCouponAddDTO addDTO);
}
