package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.dto.UserReviewAddDTO;

public interface ReviewService {
    /**
     * 用户新增评价
     * @param userId 用户id
     * @param addDTO 评价参数
     */
    void addReview(Long userId, UserReviewAddDTO addDTO);
}
