package cn.hisouten.mall.user.mapper;

import cn.hisouten.mall.user.pojo.entity.UserAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {

    /**
     * 用户地址列表查询
     * @param userId 用户id
     * @return 返回值
     */
    @Select("select * from user_address where user_id = #{userId} order by is_default")
    List<UserAddress> selectListByUserId(Long userId);
}
