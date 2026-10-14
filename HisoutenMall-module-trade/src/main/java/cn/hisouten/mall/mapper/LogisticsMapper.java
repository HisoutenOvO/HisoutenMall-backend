package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.Logistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LogisticsMapper extends BaseMapper<Logistics> {
    /**
     * 根据订单id查询物流详情
     * @param orderId 订单id
     * @param userId 用户id
     * @return 返回值
     */
    @Select("select * from logistics where order_id = #{orderId}")
    Logistics selectByOrderNo(Long userId,Long orderId);
}
