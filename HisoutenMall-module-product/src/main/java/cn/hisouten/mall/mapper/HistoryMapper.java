package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.UserBrowseHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HistoryMapper extends BaseMapper<UserBrowseHistory> {
    /**
     * 根据商品id查询浏览记录
     * @param userId 用户id
     * @param productId 商品id
     * @return 返回值
     */
    @Select("select * from user_browse_history where user_id = #{userId} and product_id = #{productId}")
    UserBrowseHistory selectByProductId(Long userId, Long productId);

    /**
     * 插入或更新浏览历史
     * @param userId 用户id
     * @param productId 商品id
     */
    @Insert("insert into user_browse_history (user_id, product_id, browse_time) values (#{userId}, #{productId}, NOW()) on duplicate key update browse_time = NOW()")
    void upsert(Long userId, Long productId);
}
