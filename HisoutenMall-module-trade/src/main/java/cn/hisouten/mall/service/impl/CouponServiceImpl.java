package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.CouponMapper;
import cn.hisouten.mall.mapper.UserCouponMapper;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.pojo.bo.coupon.CouponPageQueryBO;
import cn.hisouten.mall.pojo.bo.coupon.CouponPageResultBO;
import cn.hisouten.mall.pojo.dto.coupon.*;
import cn.hisouten.mall.pojo.entity.Coupon;
import cn.hisouten.mall.pojo.entity.UserCoupon;
import cn.hisouten.mall.pojo.vo.coupon.*;
import cn.hisouten.mall.service.CouponService;
import cn.hisouten.mall.user.service.MerchantProfileService;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.hisouten.mall.constant.CommonConstant.AMOUNT_OFF_COUPON;
import static cn.hisouten.mall.constant.CommonConstant.DISCOUNT_COUPON;
import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.RoleConstant.ADMIN_ROLE;
import static cn.hisouten.mall.constant.StatusConstant.*;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;

    private final MerchantProfileService merchantProfileService;

    /**
     * 商家新增优惠券
     * @param merchantId 商家id
     * @param addDTO 新增参数
     */
    @Override
    public void addCoupon(Long merchantId, MerchantCouponAddDTO addDTO) {
        Coupon coupon = new Coupon();
        Coupon existedCoupon = couponMapper.selectByName(merchantId,addDTO.getName());
        if(existedCoupon != null){
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
        //且减满金额不能大于最低金额，不能出现满100减200
        if(coupon.getValue().compareTo(coupon.getMinAmount()) >= 0){
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
        if(!Objects.equals(coupon.getName(), updateDTO.getName())){
            Coupon existed = couponMapper.selectByName(merchantId, updateDTO.getName());
            if (existed != null) throw new BizException(COUPON_NAME_EXIST);
        }
        coupon.setName(updateDTO.getName());
        coupon.setEndTime(updateDTO.getEndTime());
        couponMapper.updateById(coupon);
    }

    /**
     * 商家端分页查询已有的优惠券
     * @param merchantId 商家id
     * @param pageQueryDTO 条件参数
     * @return 返回值
     */
    @Override
    public PageResult<MerchantCouponPageResultVO> merchantPageQuery(Long merchantId, MerchantCouponPageQueryDTO pageQueryDTO) {
        Page<CouponPageResultBO> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        CouponPageQueryBO pageQueryBO = new CouponPageQueryBO();
        BeanUtils.copyProperties(pageQueryDTO,pageQueryBO);
        Page<CouponPageResultBO> result = couponMapper.pageQuery(merchantId,page,pageQueryBO);
        if(result == null){
            return null;
        }
        long total = result.getTotal();
        List<MerchantCouponPageResultVO> records = new ArrayList<>();
        for (CouponPageResultBO record : result.getRecords()) {
            MerchantCouponPageResultVO vo = new MerchantCouponPageResultVO();
            BeanUtils.copyProperties(record,vo);
            if(record.getStartTime().isAfter(LocalDateTime.now())){
                vo.setStatus(NOT_START);
            } else if (record.getStartTime().isBefore(LocalDateTime.now()) && record.getEndTime().isAfter(LocalDateTime.now())) {
                vo.setStatus(IN_PROGRESS);
            }else{
                vo.setStatus(FINISHED);
            }
            records.add(vo);
        }
        return new PageResult<>(total,records);
    }

    /**
     * 商家端查询优惠券详情
     * @param merchantId 商家id
     * @param couponId 优惠券
     * @return 返回值
     */
    @Override
    public MerchantCouponDetailVO merchantDetailQuery(Long merchantId, Long couponId) {
        Coupon coupon = couponMapper.selectById(couponId);
        if(coupon == null || !coupon.getMerchantId().equals(merchantId)){
            throw new BizException(COUPON_NOT_FOUND);
        }
        MerchantCouponDetailVO detailVO = new MerchantCouponDetailVO();
        BeanUtils.copyProperties(coupon,detailVO);
        if(coupon.getStartTime().isAfter(LocalDateTime.now())){
            detailVO.setStatus(NOT_START);
        } else if (coupon.getStartTime().isBefore(LocalDateTime.now()) && coupon.getEndTime().isAfter(LocalDateTime.now())) {
            detailVO.setStatus(IN_PROGRESS);
        }else{
            detailVO.setStatus(FINISHED);
        }
        return detailVO;
    }

    /**
     * 商家删除优惠券
     * @param merchantId 商家id
     * @param couponId 优惠券id
     */
    @Override
    public void deleteCoupon(Long merchantId, Long couponId) {
        Coupon coupon = couponMapper.selectById(couponId);
        if(coupon == null || !coupon.getMerchantId().equals(merchantId)){
            throw new BizException(COUPON_NOT_FOUND);
        }
        if (coupon.getStartTime().isBefore(LocalDateTime.now()) && coupon.getEndTime().isAfter(LocalDateTime.now())){
            throw new BizException(COUPON_HAS_START);
        }
        UserCoupon userCoupon = userCouponMapper.selectByCouponId(couponId);
        if(userCoupon != null){
            throw new BizException(COUPON_HAS_RECEIVED);
        }
        couponMapper.deleteById(couponId);
    }

    /**
     * 用户分页查询可领取优惠券列表
     * @param userId 用户id
     * @param pageQueryDTO 查询参数
     * @return 返回值
     */
    @Override
    public PageResult<AvailableCouponPageResultVO> availablePageQuery(Long userId, AvailableCouponPageQueryDTO pageQueryDTO) {
        Page<AvailableCouponPageResultVO> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        Page<AvailableCouponPageResultVO> result = couponMapper.availablePageQuery(page,pageQueryDTO);
        long total = result.getTotal();

        //获取用户已有优惠券列表
        List<UserCoupon> userCouponList = userCouponMapper.selectListByUserId(userId);
        List<Long> userCouponIdList = CollUtil.getFieldValues(userCouponList,"couponId", Long.class);
        //将所有优惠券进行比对，如果优惠券id存在于已有优惠券，则用户已领过
        List<AvailableCouponPageResultVO> records = new ArrayList<>();
        for (AvailableCouponPageResultVO record : result.getRecords()) {
            AvailableCouponPageResultVO vo = new AvailableCouponPageResultVO();
            BeanUtils.copyProperties(record,vo);
            boolean exist = userCouponIdList.contains(record.getId());
            vo.setReceived(exist);
            records.add(vo);
        }
        return new PageResult<>(total,records);
    }

    /**
     * 用户领券
     * @param userId 用户id
     * @param couponId 优惠券id
     */
    @Override
    public void receiveCoupon(Long userId, Long couponId) {
        Coupon coupon = couponMapper.selectById(couponId);
        if(coupon == null || coupon.getRemain() <= 0){
            throw new BizException(COUPON_NOT_FOUND);
        }
        //查看是否在有效期内
        if(!(coupon.getStartTime().isBefore(LocalDateTime.now()) && coupon.getEndTime().isAfter(LocalDateTime.now()))){
            throw new BizException(COUPON_TIME_INVALID);
        }
        UserCoupon existedCoupon = userCouponMapper.selectByCouponIdAndUserId(userId,couponId);
        if(existedCoupon != null){
            throw new BizException(COUPON_HAS_RECEIVED);
        }
        //用乐观锁扣减库存
        int affected = couponMapper.deductRemain(couponId);
        if (affected == 0) {
            throw new BizException(OUT_OF_STOCK);
        }
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus(NOT_USED);
        userCoupon.setReceiveTime(LocalDateTime.now());
        userCoupon.setExpireTime(coupon.getEndTime());
        userCouponMapper.insert(userCoupon);
    }

    /**
     * 用户查询可用优惠券列表
     * @param userId 用户id
     * @param merchantId 商家id
     * @param orderAmount 订单金额
     * @return 返回值
     */
    @Override
    public List<UserUsableCouponVO> usableQuery(Long userId, Long merchantId, BigDecimal orderAmount) {
        List<UserCoupon> userCouponList = userCouponMapper.selectListByUserId(userId);
        List<Long> couponIdList = CollUtil.getFieldValues(userCouponList,"couponId", Long.class);
        //根据优惠券id查询优惠券列表
        if(couponIdList.isEmpty()){
            return new ArrayList<>();
        }
        List<Coupon> couponList = couponMapper.selectListByIds(couponIdList);
        Map<Long,Coupon> userCouponMap = CollStreamUtil.toIdentityMap(couponList,Coupon::getId);
        List<UserUsableCouponVO> voList = new ArrayList<>();
        //拿到所有优惠券
        for (UserCoupon userCoupon : userCouponList) {
            Coupon coupon = userCouponMap.get(userCoupon.getCouponId());
            if(coupon == null) continue;
            if(userCoupon.getStatus() != NOT_USED) continue;
            if (userCoupon.getExpireTime().isBefore(LocalDateTime.now())) continue;
            if(!coupon.getMerchantId().equals(merchantId)) continue;
            if(orderAmount.compareTo(coupon.getMinAmount()) < 0) continue;
            UserUsableCouponVO vo = new UserUsableCouponVO();
            vo.setUserCouponId(userCoupon.getId());
            vo.setName(coupon.getName());
            vo.setType(coupon.getType());
            vo.setValue(coupon.getValue());
            vo.setMinAmount(coupon.getMinAmount());
            vo.setExpireTime(userCoupon.getExpireTime());
            if(coupon.getType() == AMOUNT_OFF_COUPON){
                vo.setDiscountAmount(coupon.getValue());
            }else{
                //保留两位小数
                vo.setDiscountAmount(orderAmount.subtract(orderAmount.multiply(coupon.getValue())).setScale(2, RoundingMode.HALF_UP));
            }
            voList.add(vo);
        }
        return voList;
    }


    /**
     * 管理员分页条件查询全平台优惠券
     * @param adminId 管理员id
     * @param pageQueryDTO 分页条件参数
     * @return 返回值
     */
    @Override
    public PageResult<AdminCouponPageResultVO> adminPageQuery(Long adminId, AdminCouponPageQueryDTO pageQueryDTO) {
        Page<CouponPageResultBO> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        CouponPageQueryBO pageQueryBO = new CouponPageQueryBO();
        BeanUtils.copyProperties(pageQueryDTO,pageQueryBO);
        Page<CouponPageResultBO> result = couponMapper.pageQuery(null,page,pageQueryBO);
        if(result == null){
            return null;
        }
        long total = result.getTotal();
        List<AdminCouponPageResultVO> records = new ArrayList<>();
        for (CouponPageResultBO record : result.getRecords()) {
            AdminCouponPageResultVO vo = new AdminCouponPageResultVO();
            BeanUtils.copyProperties(record,vo);
            if(record.getStartTime().isAfter(LocalDateTime.now())){
                vo.setStatus(NOT_START);
            } else if (record.getStartTime().isBefore(LocalDateTime.now()) && record.getEndTime().isAfter(LocalDateTime.now())) {
                vo.setStatus(IN_PROGRESS);
            }else{
                vo.setStatus(FINISHED);
            }
            records.add(vo);
        }
        return new PageResult<>(total,records);
    }

    /**
     * 管理员查询优惠券详情
     * @param couponId 优惠券id
     * @return 返回值
     */
    @Override
    public AdminCouponDetailVO adminDetailQuery(Long couponId) {
        Coupon coupon = couponMapper.selectById(couponId);
        if(coupon == null){
            throw new BizException(COUPON_NOT_FOUND);
        }
        AdminCouponDetailVO detailVO = new AdminCouponDetailVO();
        BeanUtils.copyProperties(coupon,detailVO);
        detailVO.setMerchantName(merchantProfileService.getMerchantNameByMerchantId(coupon.getMerchantId()));
        if(coupon.getStartTime().isAfter(LocalDateTime.now())){
            detailVO.setStatus(NOT_START);
        } else if (coupon.getStartTime().isBefore(LocalDateTime.now()) && coupon.getEndTime().isAfter(LocalDateTime.now())) {
            detailVO.setStatus(IN_PROGRESS);
        }else{
            detailVO.setStatus(FINISHED);
        }
        return detailVO;
    }
}
