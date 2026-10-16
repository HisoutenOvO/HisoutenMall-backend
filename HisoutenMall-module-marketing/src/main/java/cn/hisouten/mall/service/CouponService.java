package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.dto.MerchantCouponAddDTO;
import cn.hisouten.mall.pojo.dto.MerchantCouponUpdateDTO;

public interface CouponService {
    /**
     * 商家新增优惠券
     * @param merchantId 商家id
     * @param addDTO 新增参数
     */
    void addCoupon(Long merchantId, MerchantCouponAddDTO addDTO);

    /**
     * 商家修改优惠券
     * @param merchantId 商家id
     * @param updateDTO 修改参数
     * @param couponId 优惠券id
     */
    void updateCoupon(Long merchantId,Long couponId, MerchantCouponUpdateDTO updateDTO);
}
