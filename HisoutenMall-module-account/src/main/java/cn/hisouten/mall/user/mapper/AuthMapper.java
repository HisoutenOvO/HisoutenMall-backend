package cn.hisouten.mall.user.mapper;

import cn.hisouten.mall.user.pojo.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthMapper extends BaseMapper<User> {
    /**
     * 通过用户名获取用户信息
     * @param username 用户名
     * @return 返回用户对象
     */
    @Select("select * from user where username = #{username}")
    User getUserByUserName(String username);

    /**
     * 查询用户名是否存在
     * @param username 注册的用户名
     * @return 返回可能存在用户名
     */
    @Select("select username from user where username = #{username}")
    String selectExistedUserName(String username);
}
