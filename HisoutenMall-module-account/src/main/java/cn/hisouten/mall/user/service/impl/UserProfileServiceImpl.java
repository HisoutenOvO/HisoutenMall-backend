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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileMapper userProfileMapper;
    private final AuthMapper authMapper;


    /**
     * 用户查询详情
     * @param userId 用户id
     * @return 返回值
     */
    @Override
    public UserDetailVO detailQuery(Long userId) {
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
    public void updateInfo(Long userId, UserUpdateDTO userUpdateDTO) {
        UserProfile userProfile = userProfileMapper.selectProfileByUserId(userId);
        if(userProfile == null){
            throw new BizException(USER_NOT_FOUND);
        }
        BeanUtils.copyProperties(userUpdateDTO,userProfile);
        userProfileMapper.updateById(userProfile);
    }

    /**
     * 用户修改密码
     * @param userId 用户id
     * @param passwordUpdateDTO 密码
     */
    @Override
    public void updatePwd(Long userId, PasswordUpdateDTO passwordUpdateDTO) {
        User user = authMapper.selectById(userId);
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

    /**
     * 通过用户id查询用户名称
     * @param userId 用户主表id
     * @return 返回值
     */
    @Override
    public String getUserNicknameByUserId(Long userId) {
        return userProfileMapper.selectUserNicknameByUserId(userId);
    }
}
