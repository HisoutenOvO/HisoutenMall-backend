package cn.hisouten.mall.user.service.impl;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.user.mapper.UserProfileMapper;
import cn.hisouten.mall.user.pojo.dto.user.AdminUserPageQueryDTO;
import cn.hisouten.mall.user.pojo.vo.AdminUserPageResultVO;
import cn.hisouten.mall.user.service.AdminUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

import static cn.hisouten.mall.constant.StatusConstant.DISABLED;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserProfileMapper userProfileMapper;

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
}
