package cn.hisouten.mall.user.service;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.user.pojo.dto.user.AdminUserPageQueryDTO;
import cn.hisouten.mall.user.pojo.vo.AdminUserPageResultVO;

public interface AdminUserService {
    /**
     * 用户分页查询
     * @param adminUserPageQueryDTO 查询参数
     * @return 返回值
     */
    PageResult<AdminUserPageResultVO> pageQuery(AdminUserPageQueryDTO adminUserPageQueryDTO);
}
