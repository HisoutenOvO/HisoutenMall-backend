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

    /**
     * 查询用户总数
     * @return 返回值
     */
    @Select("select count(*) from user where role = 1 and deleted = 0")
    Long countTotalUsers();

    /**
     * 查询待审核商家总数
     * @return 返回值
     */
    @Select("select count(*) from merchant_profile where audit_status = 0")
    Long countPendingMerchants();

    /**
     * 根据用户id查询用户
     * @param userId 用户id
     * @return 返回值
     */
    @Select("select * from user where id = #{userId}")
    User getUserByUserId(Long userId);

    /**
     * 查询商家总数
     * @return 返回值
     */
    @Select("select count(*) from user where role = 2 and deleted = 0")
    Long countTotalMerchants();
}
