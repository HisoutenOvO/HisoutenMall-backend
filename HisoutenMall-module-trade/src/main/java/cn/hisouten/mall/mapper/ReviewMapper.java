package cn.hisouten.mall.mapper;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import cn.hisouten.mall.pojo.bo.review.ReviewPageQueryBO;
import cn.hisouten.mall.pojo.bo.review.ReviewPageResultBO;
import cn.hisouten.mall.pojo.entity.ProductReview;
import cn.hisouten.mall.pojo.vo.review.UserPendingReviewPageResultVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
     * 分页条件查询待评价列表
     * @param userId 用户id
     * @param page 分页条件
     * @param pageQueryDTO 条件查询
     * @return 返回值
     */
    Page<UserPendingReviewPageResultVO> pendingPageQuery(Long userId, Page<UserPendingReviewPageResultVO> page, BasePageQueryDTO pageQueryDTO);

    /**
     * 根据商品id查询评价列表
     * @param productId 商品id
     * @return 返回值
     */
    @Select("select * from product_review where product_id = #{productId} and deleted = 0")
    List<ProductReview> selectByProductId(Long productId);
}
