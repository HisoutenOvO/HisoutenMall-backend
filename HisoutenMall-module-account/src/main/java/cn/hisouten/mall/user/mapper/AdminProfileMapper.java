package cn.hisouten.mall.user.mapper;

import cn.hisouten.mall.user.pojo.entity.AdminProfile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminProfileMapper extends BaseMapper<AdminProfile> {

    /**
     * 根据用户id查询管理员信息
     * @param userId 用户id
     * @return 返回值
     */
    @Select("select * from admin_profile where user_id = #{userId}")
    AdminProfile selectProfileByUserId(Long userId);
}
