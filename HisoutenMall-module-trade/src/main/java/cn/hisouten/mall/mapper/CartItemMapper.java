package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.bo.CartItemListBO;
import cn.hisouten.mall.pojo.entity.CartItem;
import cn.hisouten.mall.pojo.vo.UserCartItemListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CartItemMapper extends BaseMapper<CartItem> {

    /**
     * 根据用户id和skuId查询购物车记录
     * @param userId 用户id
     * @param skuId skuId
     * @return 返回值
     */
    @Select("select * from cart_item where user_id = #{userId} and sku_id = #{skuId}")
    CartItem selectByUserIdAndSkuId(Long userId, Long skuId);

    /**
     * 查询该用户的购物车条目数量
     * @param userId 用户id
     * @return 返回值
     */
    @Select("select count(0) from cart_item where user_id = #{userId}")
    int selectCartCount(Long userId);

    /**
     * 通过用户id查找购物车列表
     * @param userId 用户id
     * @return 返回值
     */
    List<CartItemListBO> selectListByUserId(Long userId);
}
