package cn.hisouten.mall.dao.mapper;

import cn.hisouten.mall.pojo.entity.UserFavorite;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.vo.favorite.UserFavoritePageResultVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FavoriteMapper extends BaseMapper<UserFavorite> {
    /**
     * 根据用户id和商品id查询收藏商品
     * @param userId 用户id
     * @param productId 商品id
     * @return 返回值
     */
    @Select("select * from user_favorite where user_id = #{userId} and product_id = #{productId}")
    Product selectProductById(Long userId, Long productId);

    /**
     * 用户分页查询收藏
     * @param page 分页条件
     * @param userId 用户id
     * @return 返回值
     */
    Page<UserFavoritePageResultVO> pageQuery(Page<UserFavoritePageResultVO> page, Long userId);

    /**
     * 查询收藏数量
     * @param userId 用户id
     * @param productId 商品id
     * @return 返回值
     */
    Integer selectCount(Long userId, Long productId);

    /**
     * 根据商品id取消收藏
     * @param productId 商品id
     * @param userId 用户id
     */
    void deleteByProductId(Long userId,Long productId);
}
