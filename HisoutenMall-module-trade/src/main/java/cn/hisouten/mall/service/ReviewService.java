package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.UserReviewAddDTO;
import cn.hisouten.mall.pojo.dto.UserReviewPageQueryDTO;
import cn.hisouten.mall.pojo.vo.UserReviewPageResultVO;

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
    PageResult<UserReviewPageResultVO> userPageResult(UserReviewPageQueryDTO pageQueryDTO);
}
