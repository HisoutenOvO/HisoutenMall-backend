package cn.hisouten.mall.user.service;

import cn.hisouten.mall.user.pojo.dto.UserUpdateDTO;
import cn.hisouten.mall.user.pojo.vo.UserDetailVO;

public interface UserProfileService {
    /**
     * 用户查询详情
     * @param userId 用户id
     * @return 返回值
     */
    UserDetailVO detailQuery(Long userId);

    /**
     * 用户修改个人信息
     * @param userId 用户id
     * @param userUpdateDTO 修改参数
     */
    void updateInfo(Long userId, UserUpdateDTO userUpdateDTO);
}
