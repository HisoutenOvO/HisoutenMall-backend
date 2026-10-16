package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.CouponMapper;
import cn.hisouten.mall.mapper.UserCouponMapper;
import cn.hisouten.mall.pojo.dto.MerchantCouponAddDTO;
import cn.hisouten.mall.pojo.dto.MerchantCouponUpdateDTO;
import cn.hisouten.mall.pojo.entity.Coupon;
import cn.hisouten.mall.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static cn.hisouten.mall.constant.CommonConstant.AMOUNT_OFF_COUPON;
import static cn.hisouten.mall.constant.CommonConstant.DISCOUNT_COUPON;
import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;

    /**
     * 商家新增优惠券
     * @param merchantId 商家id
     * @param addDTO 新增参数
     */
    @Override
    public void addCoupon(Long merchantId, MerchantCouponAddDTO addDTO) {
        Coupon coupon = new Coupon();
        Coupon existedCoupon = couponMapper.selectByName(merchantId,addDTO.getName());
        if(Objects.equals(existedCoupon.getName(), addDTO.getName())){
            throw new BizException(COUPON_NAME_EXIST);
        }
        LocalDateTime startTime = addDTO.getStartTime();
        LocalDateTime endtime = addDTO.getEndTime();
        if(endtime.isBefore(startTime) || startTime.isBefore(LocalDateTime.now())){
            throw new BizException(COUPON_TIME_INVALID);
        }
        //若是满减券，则价值必须大于0，最低金额大于等于0
        if(addDTO.getType() == AMOUNT_OFF_COUPON && (addDTO.getValue().compareTo(BigDecimal.ZERO) <= 0 || addDTO.getMinAmount().compareTo(BigDecimal.ZERO) < 0)){
            throw new BizException(COUPON_INFO_INVALID);
        }
        //若是折扣券，则折扣必须在0-1之间
        if(addDTO.getType() == DISCOUNT_COUPON && (addDTO.getValue().compareTo(BigDecimal.ONE) >= 0 || addDTO.getValue().compareTo(BigDecimal.ZERO) <= 0)){
            throw new BizException(COUPON_INFO_INVALID);
        }
        BeanUtils.copyProperties(addDTO,coupon);
        coupon.setMerchantId(merchantId);
        coupon.setRemain(addDTO.getTotal());
        couponMapper.insert(coupon);
    }

    /**
     * 商家修改优惠券
     * @param merchantId 商家id
     * @param updateDTO 修改参数
     */
    @Override
    public void updateCoupon(Long merchantId, Long couponId,MerchantCouponUpdateDTO updateDTO) {
        Coupon coupon = couponMapper.selectById(couponId);
        if(coupon == null || !coupon.getMerchantId().equals(merchantId)){
            throw new BizException(COUPON_NOT_FOUND);
        }
        if(coupon.getEndTime().isBefore(LocalDateTime.now())){
            throw new BizException(COUPON_EXPIRE_OUT);
        }
        LocalDateTime startTime = coupon.getStartTime();
        LocalDateTime endtime = updateDTO.getEndTime();
        //判断优惠券使用时间是否开始，开始后无法修改
        if(startTime.isAfter(LocalDateTime.now())){
            throw new BizException(COUPON_HAS_START);
        }
        //校验修改参数是否合法
        if(endtime.isBefore(startTime)){
            throw new BizException(COUPON_TIME_INVALID);
        }
        if(Objects.equals(coupon.getName(), updateDTO.getName())){
            throw new BizException(COUPON_NAME_EXIST);
        }
        coupon.setName(updateDTO.getName());
        coupon.setEndTime(updateDTO.getEndTime());
        couponMapper.updateById(coupon);
    }
}
