package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.MerchantCouponAddDTO;
import cn.hisouten.mall.pojo.dto.MerchantCouponPageQueryDTO;
import cn.hisouten.mall.pojo.dto.MerchantCouponUpdateDTO;
import cn.hisouten.mall.pojo.vo.MerchantCouponPageResultVO;

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

    /**
     * 商家端分页查询已有的优惠券
     * @param merchantId 商家id
     * @param pageQueryDTO 条件参数
     * @return 返回值
     */
    PageResult<MerchantCouponPageResultVO> merchantPageQuery(Long merchantId, MerchantCouponPageQueryDTO pageQueryDTO);
}
