package cn.hisouten.mall.user.mapper;

import cn.hisouten.mall.user.pojo.dto.AdminUserPageQueryDTO;
import cn.hisouten.mall.user.pojo.entity.UserProfile;
import cn.hisouten.mall.user.pojo.vo.AdminUserPageResultVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {
    /**
     * 分页查询用户
     * @param page 分页条件
     * @param adminUserPageQueryDTO 查询条件
     * @return 返回VO，没办法了，也不能再写个一模一样的BO，也不能n+1，只能直接返VO了
     */
    Page<AdminUserPageResultVO> pageQuery(Page<AdminUserPageResultVO> page,@Param("dto") AdminUserPageQueryDTO adminUserPageQueryDTO);
}
