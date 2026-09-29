package cn.hisouten.mall.user.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.user.mapper.AuthMapper;
import cn.hisouten.mall.user.mapper.UserProfileMapper;
import cn.hisouten.mall.user.pojo.entity.User;
import cn.hisouten.mall.user.pojo.entity.UserProfile;
import cn.hisouten.mall.user.pojo.vo.UserDetailVO;
import cn.hisouten.mall.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.USER_NOT_FOUND;

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
}
