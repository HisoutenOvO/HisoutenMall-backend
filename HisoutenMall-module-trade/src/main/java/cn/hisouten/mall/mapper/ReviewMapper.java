package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.ProductReview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReviewMapper extends BaseMapper<ProductReview> {

    /**
     * 根据订单项id查询评价
     * @param userId 用户id
     * @param orderItemId 订单项id
     * @return 返回值
     */
    @Select("select * from product_review where user_id = #{userId} and order_item_id = #{orderItemId}")
    ProductReview selectByItemId(Long userId, Long orderItemId);
}
