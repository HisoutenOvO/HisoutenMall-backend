package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.OrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    /**
     * 根据订单id查找各订单项
     * @param orderId 订单id
     * @return 返回值
     */
    @Select("select * from order_item where order_id = #{orderId}")
    List<OrderItem> selectByOrderId(Long orderId);
}
