package cn.hisouten.mall.user.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.user.mapper.AuthMapper;
import cn.hisouten.mall.user.mapper.UserProfileMapper;
import cn.hisouten.mall.user.pojo.dto.UserUpdateDTO;
import cn.hisouten.mall.user.pojo.dto.PasswordUpdateDTO;
import cn.hisouten.mall.user.pojo.entity.User;
import cn.hisouten.mall.user.pojo.entity.UserProfile;
import cn.hisouten.mall.user.pojo.vo.UserDetailVO;
import cn.hisouten.mall.user.service.UserProfileService;
import cn.hisouten.mall.util.CacheClientUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.RedisConstant.*;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileMapper userProfileMapper;
    private final AuthMapper authMapper;

    private final CacheClientUtils cacheClientUtils;


    /**
     * 用户查询详情
     * @param userId 用户id
     * @return 返回值
     */
    @Override
    public UserDetailVO detailQuery(Long userId) {
        return cacheClientUtils.queryWithPassThrough(
                CACHE_USER_DETAIL_PREFIX,
                userId,
                UserDetailVO.class,
                this::loadDetailFromDB,
                CACHE_USER_DETAIL_TTL,
                TimeUnit.MINUTES
        );
    }

    private UserDetailVO loadDetailFromDB(Long userId){
        User user = authMapper.selectById(userId);
        if(user == null){
            throw new BizException(USER_NOT_FOUND);
        }
        UserProfile userProfile = userProfileMapper.selectProfileByUserId(userId);
        if(userProfile == null){
            throw new BizException(USER_NOT_FOUND);
        }
        return UserDetailVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(userProfile.getNickname())
                .avatar(userProfile.getAvatar())
                .phone(userProfile.getPhone())
                .email(userProfile.getEmail())
                .gender(userProfile.getGender())
                .birthday(userProfile.getBirthday())
                .build();
    }
    /**
     * 用户修改个人信息
     * @param userId 用户id
     * @param userUpdateDTO 修改参数
     */
    @Override
    @CacheEvict(value = CACHE_USER_NICKNAME_PREFIX, key = "#userId")
    public void updateInfo(Long userId, UserUpdateDTO userUpdateDTO) {
        UserProfile userProfile = userProfileMapper.selectProfileByUserId(userId);
        if(userProfile == null){
            throw new BizException(USER_NOT_FOUND);
        }
        BeanUtils.copyProperties(userUpdateDTO,userProfile);
        userProfileMapper.updateById(userProfile);
    }

    /**
     * 通过用户id查询用户名称
     * @param userId 用户主表id
     * @return 返回值
     */
    @Override
    @Cacheable(cacheNames = CACHE_USER_NICKNAME_PREFIX, key = "#userId")
    public String getUserNicknameByUserId(Long userId) {
        return userProfileMapper.selectUserNicknameByUserId(userId);
    }
}
