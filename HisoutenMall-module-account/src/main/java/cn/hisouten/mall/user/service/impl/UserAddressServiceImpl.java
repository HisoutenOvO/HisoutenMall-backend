package cn.hisouten.mall.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.user.mapper.UserAddressMapper;
import cn.hisouten.mall.user.pojo.dto.UserAddressAddDTO;
import cn.hisouten.mall.user.pojo.dto.UserAddressUpdateDTO;
import cn.hisouten.mall.user.pojo.entity.UserAddress;
import cn.hisouten.mall.user.pojo.vo.UserAddressDetailVO;
import cn.hisouten.mall.user.pojo.vo.UserAddressListVO;
import cn.hisouten.mall.user.service.UserAddressService;
import cn.hisouten.mall.util.CacheClientUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.RedisConstant.CACHE_ADDRESS_DETAIL_PREFIX;
import static cn.hisouten.mall.constant.RedisConstant.CACHE_ADDRESS_DETAIL_TTL;
import static cn.hisouten.mall.constant.StatusConstant.ENABLED;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {
    private final UserAddressMapper userAddressMapper;
    private final CacheClientUtils cacheClientUtils;


    /**
     * 用户地址列表查询
     * @param userId 用户id
     * @return 返回值
     */
    @Override
    public List<UserAddressListVO> listQuery(Long userId) {
        List<UserAddress> userAddressList = userAddressMapper.selectListByUserId(userId);
        List<UserAddressListVO> userAddressListVOS = new ArrayList<>();
        for (UserAddress userAddress : userAddressList) {
            UserAddressListVO vo = new UserAddressListVO();
            vo.setId(userAddress.getId());
            vo.setReceiveName(userAddress.getReceiverName());
            vo.setProvince(userAddress.getProvince());
            vo.setCity(userAddress.getCity());
            vo.setDistrict(userAddress.getDistrict());
            vo.setDetail(userAddress.getDetail());
            vo.setIsDefault(userAddress.getIsDefault());
            userAddressListVOS.add(vo);
        }
        return userAddressListVOS;
    }

    /**
     * 用户查询地址详情
     * @param addressId 地址id
     * @return 返回值
     */
    @Override
    public UserAddressDetailVO detailQuery(Long addressId) {
        return cacheClientUtils.queryWithPassThrough(
                CACHE_ADDRESS_DETAIL_PREFIX,
                addressId,
                UserAddressDetailVO.class,
                this::loadDetailFromDB,
                CACHE_ADDRESS_DETAIL_TTL,
                TimeUnit.MINUTES
        );
    }

    private UserAddressDetailVO loadDetailFromDB(Long addressId){
        UserAddress userAddress = userAddressMapper.selectById(addressId);
        if(userAddress == null){
            throw new BizException(ADDRESS_NOT_FOUND);
        }
        if(!userAddress.getUserId().equals(StpUtil.getLoginIdAsLong())){
            throw new BizException(NO_PERMISSION);
        }
        UserAddressDetailVO userAddressDetailVO = new UserAddressDetailVO();
        BeanUtils.copyProperties(userAddress,userAddressDetailVO);
        return userAddressDetailVO;
    }

    /**
     * 用户新增地址
     * @param userId 用户id
     * @param userAddressAddDTO 用户新增地址参数
     */
    @Override
    @Transactional
    public void addAddress(Long userId, UserAddressAddDTO userAddressAddDTO) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressAddDTO,userAddress);
        if(userAddressAddDTO.getIsDefault() == ENABLED){
            userAddressMapper.changeDefaultAddress(userId);
        }
        userAddress.setUserId(userId);
        userAddressMapper.insert(userAddress);
    }

    /**
     * 用户修改地址
     * @param addressId 地址id
     * @param userAddressUpdateDTO 修改参数
     */
    @Override
    @Transactional
    public void updateAddress(Long addressId, UserAddressUpdateDTO userAddressUpdateDTO) {
        UserAddress userAddress = userAddressMapper.selectById(addressId);
        if(userAddress == null){
            throw new BizException(ADDRESS_NOT_FOUND);
        }
        if(!userAddress.getUserId().equals(StpUtil.getLoginIdAsLong())){
            throw new BizException(NO_PERMISSION);
        }
        BeanUtils.copyProperties(userAddressUpdateDTO,userAddress);
        if(userAddressUpdateDTO.getIsDefault() == ENABLED){
            userAddressMapper.changeDefaultAddress(userAddress.getUserId());
        }
        userAddressMapper.updateById(userAddress);
    }

    /**
     * 用户修改默认地址
     * @param addressId 地址id
     */
    @Override
    @Transactional
    public void changeDefault(Long addressId) {
        UserAddress userAddress = userAddressMapper.selectById(addressId);
        if(userAddress == null){
            throw new BizException(ADDRESS_NOT_FOUND);
        }
        if(!userAddress.getUserId().equals(StpUtil.getLoginIdAsLong())){
            throw new BizException(NO_PERMISSION);
        }
        userAddressMapper.changeDefaultAddress(userAddress.getUserId());
        userAddress.setIsDefault(ENABLED);
        userAddressMapper.updateById(userAddress);
    }

    /**
     * 用户删除地址
     * @param addressId 地址id
     */
    @Override
    public void deleteAddress(Long addressId) {
        UserAddress userAddress = userAddressMapper.selectById(addressId);
        if(userAddress == null){
            throw new BizException(ADDRESS_NOT_FOUND);
        }
        if(!userAddress.getUserId().equals(StpUtil.getLoginIdAsLong())){
            throw new BizException(NO_PERMISSION);
        }
        if(userAddress.getIsDefault() == ENABLED){
            throw new BizException(CHANGE_DEFAULT_ADDRESS_FIRST);
        }
        userAddressMapper.deleteById(addressId);
    }

    /**
     * 根据id查询地址信息
     * @param addressId 地址id
     * @return 返回值
     */
    @Override
    public UserAddress getById(Long addressId) {
        return userAddressMapper.selectById(addressId);
    }


}
