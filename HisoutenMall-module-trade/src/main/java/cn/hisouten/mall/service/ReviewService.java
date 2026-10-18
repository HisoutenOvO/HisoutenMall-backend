package cn.hisouten.mall.service;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.review.AdminReviewPageQueryDTO;
import cn.hisouten.mall.pojo.dto.review.UserReviewAddDTO;
import cn.hisouten.mall.pojo.dto.review.ReviewPageQueryDTO;
import cn.hisouten.mall.pojo.vo.review.*;

public interface ReviewService {
    /**
     * 用户新增评价
     * @param userId 用户id
     * @param addDTO 评价参数
     */
    void addReview(Long userId, UserReviewAddDTO addDTO);

    /**
     * 用户分页查询评价
     * @param pageQueryDTO 分页查询参数
     * @return 返回值
     */
    PageResult<UserReviewPageResultVO> userPageQuery(ReviewPageQueryDTO pageQueryDTO);

    /**
     * 用户分页查询自己发过的评价列表
     * @param userId 用户id
     * @param pageQueryDTO 分页参数
     * @return 返回值
     */
    PageResult<UserSelfReviewPageResultVO> selfPageQuery(Long userId, ReviewPageQueryDTO pageQueryDTO);

    /**
     * 商家端分页查询自己的评价列表
     * @param merchantId 商家id
     * @param pageQueryDTO 分页参数
     * @return 返回值
     */
    PageResult<MerchantReviewPageResultVO> merchantPageQuery(Long merchantId, ReviewPageQueryDTO pageQueryDTO);

    /**
     * 用户删除评价
     * @param userId 用户id
     * @param reviewId 评价id
     */
    void deleteReview(Long userId, Long reviewId);

    /**
     * 用户分页查询待评价商品
     * @param userId 用户id
     * @param pageQueryDTO 查询条件
     * @return 返回值
     */
    PageResult<UserPendingReviewPageResultVO> pendingPageQuery(Long userId, BasePageQueryDTO pageQueryDTO);

    /**
     * 商家端查询商品评价统计
     * @param merchantId 商家id
     * @param productId 商品id
     * @return 返回值
     */
    MerchantReviewStatisticsVO statisticsQuery(Long merchantId, Long productId);

    /**
     * 管理端分页查询评价
     * @param adminId 管理员id
     * @param pageQueryDTO 分页查询条件
     * @return 返回值
     */
    PageResult<AdminReviewPageResultVO> adminPageQuery(Long adminId, AdminReviewPageQueryDTO pageQueryDTO);

    /**
     * 管理员强制删除评价
     * @param adminId 管理员id
     * @param reviewId 评价id
     */
    void adminDeleteReview(Long adminId, Long reviewId);

    /**
     * 用户端查询评价详情
     * @param userId 商家id
     * @param reviewId 评价id
     * @return 返回值
     */
    ReviewDetailVO detailQuery(Long userId, Long reviewId);
}
