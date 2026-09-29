package cn.hisouten.mall.user.service;

import cn.hisouten.mall.user.pojo.vo.UserDetailVO;

public interface UserProfileService {
    /**
     * 用户查询详情
     * @param userId 用户id
     * @return 返回值
     */
    UserDetailVO detailQuery(Long userId);
}
