package cn.hisouten.mall.service;

import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.coupon.*;
import cn.hisouten.mall.pojo.vo.coupon.*;

import java.math.BigDecimal;
import java.util.List;

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

    /**
     * 商家端查询优惠券详情
     * @param merchantId 商家id
     * @param couponId 优惠券
     * @return 返回值
     */
    MerchantCouponDetailVO merchantDetailQuery(Long merchantId, Long couponId);

    /**
     * 商家删除优惠券
     * @param merchantId 商家id
     * @param couponId 优惠券id
     */
    void deleteCoupon(Long merchantId, Long couponId);

    /**
     * 用户分页查询可领取优惠券列表
     * @param userId 用户id
     * @param pageQueryDTO 查询参数
     * @return 返回值
     */
    PageResult<AvailableCouponPageResultVO> availablePageQuery(Long userId, AvailableCouponPageQueryDTO pageQueryDTO);

    /**
     * 用户领券
     * @param userId 用户id
     * @param couponId 优惠券id
     */
    void receiveCoupon(Long userId, Long couponId);

    /**
     * 用户查询可用优惠券列表
     * @param userId 用户id
     * @param merchantId 商家id
     * @param orderAmount 订单金额
     * @return 返回值
     */
    List<UserUsableCouponVO> usableQuery(Long userId, Long merchantId, BigDecimal orderAmount);

    /**
     * 管理员分页条件查询全平台优惠券
     * @param adminId 管理员id
     * @param pageQueryDTO 分页条件参数
     * @return 返回值
     */
    PageResult<AdminCouponPageResultVO> adminPageQuery(Long adminId, AdminCouponPageQueryDTO pageQueryDTO);
}
