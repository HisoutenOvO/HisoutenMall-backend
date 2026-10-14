package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.OrderItemMapper;
import cn.hisouten.mall.mapper.OrderMapper;
import cn.hisouten.mall.mapper.ReviewMapper;
import cn.hisouten.mall.pojo.dto.UserReviewAddDTO;
import cn.hisouten.mall.pojo.entity.Order;
import cn.hisouten.mall.pojo.entity.OrderItem;
import cn.hisouten.mall.pojo.entity.ProductReview;
import cn.hisouten.mall.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static cn.hisouten.mall.constant.CommonConstant.DEFAULT_RATING;
import static cn.hisouten.mall.constant.CommonConstant.DEFAULT_REVIEW;
import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.StatusConstant.COMPLETED;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewMapper reviewMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    /**
     * 用户新增评价
     * @param userId 用户id
     * @param addDTO 评价参数
     */
    @Override
    public void addReview(Long userId, UserReviewAddDTO addDTO) {
        OrderItem orderItem = orderItemMapper.selectById(addDTO.getOrderItemId());
        if(orderItem == null){
            throw new BizException(ORDER_NOT_FOUND);
        }
        Order order = orderMapper.selectById(orderItem.getOrderId());
        if(order == null || !order.getUserId().equals(userId)){
            throw new BizException(ORDER_NOT_FOUND);
        }
        if(order.getStatus() != COMPLETED){
            throw new BizException(ORDER_STATUS_ERROR);
        }
        ProductReview existedReview = reviewMapper.selectByItemId(userId,orderItem.getId());
        if(existedReview != null){
            throw new BizException(REVIEW_ALREADY_EXIST);
        }
        ProductReview review = new ProductReview();
        review.setUserId(userId);
        review.setProductId(orderItem.getProductId());
        review.setSkuId(orderItem.getSkuId());
        review.setSkuSpecs(orderItem.getSkuSpecs());
        review.setOrderId(order.getId());
        review.setOrderItemId(orderItem.getId());
        review.setRating(addDTO.getRating());
        if(addDTO.getRating() == null){
            review.setRating(DEFAULT_RATING);
        }
        review.setContent(addDTO.getContent());
        if(addDTO.getContent() == null){
            review.setContent(DEFAULT_REVIEW);
        }
        review.setImage(addDTO.getImage());
        reviewMapper.insert(review);
    }
}
