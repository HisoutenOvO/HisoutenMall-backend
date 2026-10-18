package cn.hisouten.mall.user.service;

import cn.hisouten.mall.user.pojo.entity.AdminProfile;
import cn.hisouten.mall.user.pojo.vo.admin.AdminProfileDetailVO;

public interface AdminProfileService {

    /**
     * 管理员查询详细信息
     * @param adminId 管理员id
     * @return 返回值
     */
    AdminProfileDetailVO detailQuery(Long adminId);
}
