package cn.hisouten.mall.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.user.mapper.AuthMapper;
import cn.hisouten.mall.user.mapper.UserProfileMapper;
import cn.hisouten.mall.user.pojo.dto.AdminUserPageQueryDTO;
import cn.hisouten.mall.user.pojo.entity.User;
import cn.hisouten.mall.user.pojo.vo.AdminUserPageResultVO;
import cn.hisouten.mall.user.service.AdminUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.ROLE_NOT_USER;
import static cn.hisouten.mall.constant.ExceptionMessageConstant.USER_NOT_FOUND;
import static cn.hisouten.mall.constant.RoleConstant.USER_ROLE;
import static cn.hisouten.mall.constant.StatusConstant.DISABLED;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserProfileMapper userProfileMapper;
    private final AuthMapper authMapper;

    /**
     * 用户分页查询
     * @param adminUserPageQueryDTO 查询参数
     * @return 返回值
     */
    @Override
    public PageResult<AdminUserPageResultVO> pageQuery(AdminUserPageQueryDTO adminUserPageQueryDTO) {
        Page<AdminUserPageResultVO> page = new Page<>(adminUserPageQueryDTO.getPage(), adminUserPageQueryDTO.getPageSize());
        if(adminUserPageQueryDTO.getDeleted() == null){
            adminUserPageQueryDTO.setDeleted(DISABLED);
        }
        Page<AdminUserPageResultVO> result = userProfileMapper.pageQuery(page,adminUserPageQueryDTO);
        long total = result.getTotal();
        List<AdminUserPageResultVO> records = result.getRecords();
        return new PageResult<>(total,records);
    }

    /**
     * 改变用户状态
     * @param userId 用户id
     * @param status 状态
     */
    @Override
    public void changeStatus(Long userId, Integer status) {
        User user = authMapper.selectById(userId);
        if(user == null){
            throw new BizException(USER_NOT_FOUND);
        }
        if(!user.getRole().equals(USER_ROLE)){
            throw new BizException(ROLE_NOT_USER);
        }
        user.setStatus(status);
        authMapper.updateById(user);
        StpUtil.kickout(userId);

    }
}
