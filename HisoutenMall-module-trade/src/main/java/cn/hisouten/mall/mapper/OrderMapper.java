package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 根据订单号查找订单
     * @param orderNo 订单号
     * @return 返回值
     */
    @Select("select * from `order` where order_no = #{orderNo}")
    Order selectByOrderNo(String orderNo);
}
