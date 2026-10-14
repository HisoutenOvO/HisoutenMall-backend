package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.BasePageQueryDTO;
import cn.hisouten.mall.pojo.bo.ReviewPageQueryBO;
import cn.hisouten.mall.pojo.bo.ReviewPageResultBO;
import cn.hisouten.mall.pojo.entity.ProductReview;
import cn.hisouten.mall.pojo.vo.UserPendingReviewPageResultVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 分页条件查询评价列表
     * @param page 分页参数
     * @param pageQueryBO 条件参数
     * @return 返回值
     */
    Page<ReviewPageResultBO> pageQuery(Page<ReviewPageResultBO> page, ReviewPageQueryBO pageQueryBO);

    /**
     *
     * @param userId
     * @param page
     * @param pageQueryDTO
     * @return
     */
    Page<UserPendingReviewPageResultVO> pendingPageQuery(Long userId, Page<UserPendingReviewPageResultVO> page, BasePageQueryDTO pageQueryDTO);
}
