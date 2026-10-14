package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.UserReviewAddDTO;
import cn.hisouten.mall.pojo.dto.ReviewPageQueryDTO;
import cn.hisouten.mall.pojo.vo.MerchantReviewPageResultVO;
import cn.hisouten.mall.pojo.vo.UserReviewPageResultVO;
import cn.hisouten.mall.pojo.vo.UserSelfReviewPageResultVO;

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
    PageResult<UserReviewPageResultVO> userPageResult(ReviewPageQueryDTO pageQueryDTO);

    /**
     * 用户分页查询自己发过的评价列表
     * @param userId 用户id
     * @param pageQueryDTO 分页参数
     * @return 返回值
     */
    PageResult<UserSelfReviewPageResultVO> selfPageResult(Long userId, ReviewPageQueryDTO pageQueryDTO);

    /**
     * 商家端分页查询自己的评价列表
     * @param merchantId 商家id
     * @param pageQueryDTO 分页参数
     * @return 返回值
     */
    PageResult<MerchantReviewPageResultVO> merchantPageResult(Long merchantId, ReviewPageQueryDTO pageQueryDTO);
}
