package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.OrderItemMapper;
import cn.hisouten.mall.mapper.OrderMapper;
import cn.hisouten.mall.mapper.ReviewMapper;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.bo.ReviewPageQueryBO;
import cn.hisouten.mall.pojo.bo.ReviewPageResultBO;
import cn.hisouten.mall.pojo.dto.UserReviewAddDTO;
import cn.hisouten.mall.pojo.dto.ReviewPageQueryDTO;
import cn.hisouten.mall.pojo.entity.Order;
import cn.hisouten.mall.pojo.entity.OrderItem;
import cn.hisouten.mall.pojo.entity.ProductReview;
import cn.hisouten.mall.pojo.vo.MerchantReviewPageResultVO;
import cn.hisouten.mall.pojo.vo.UserReviewPageResultVO;
import cn.hisouten.mall.pojo.vo.UserSelfReviewPageResultVO;
import cn.hisouten.mall.service.ReviewService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        review.setImage(addDTO.getImages());
        reviewMapper.insert(review);
    }

    /**
     * 用户端游客分页查询评价
     * @param pageQueryDTO 分页查询参数
     * @return 返回值
     */
    @Override
    public PageResult<UserReviewPageResultVO> userPageResult(ReviewPageQueryDTO pageQueryDTO) {
        Page<ReviewPageResultBO> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        ReviewPageQueryBO pageQueryBO = new ReviewPageQueryBO();
        pageQueryBO.setProductId(pageQueryDTO.getProductId());
        pageQueryBO.setRating(pageQueryBO.getRating());
        Page<ReviewPageResultBO> result = reviewMapper.pageQuery(page,pageQueryBO);
        long total = result.getTotal();
        List<ReviewPageResultBO> boList = result.getRecords();
        List<UserReviewPageResultVO> voList = new ArrayList<>();
        for (ReviewPageResultBO bo : boList) {
            UserReviewPageResultVO vo = new UserReviewPageResultVO();
            String image = bo.getImages();
            //转成单个字符串
            List<String> images = StrUtil.split(image,",");
            vo.setImages(images);
            //脱敏处理
            vo.setNickname(StrUtil.hide(bo.getNickname(),1,bo.getNickname().length()));
            BeanUtils.copyProperties(bo,vo);
            voList.add(vo);
        }
        return new PageResult<>(total,voList);
    }

    /**
     * 用户分页查询自己发过的评价列表
     * @param userId 用户id
     * @param pageQueryDTO 分页参数
     * @return 返回值
     */
    @Override
    public PageResult<UserSelfReviewPageResultVO> selfPageResult(Long userId, ReviewPageQueryDTO pageQueryDTO) {
        Page<ReviewPageResultBO> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        ReviewPageQueryBO pageQueryBO = new ReviewPageQueryBO();
        pageQueryBO.setProductId(pageQueryDTO.getProductId());
        pageQueryBO.setRating(pageQueryBO.getRating());
        pageQueryBO.setUserId(userId);
        Page<ReviewPageResultBO> result = reviewMapper.pageQuery(page,pageQueryBO);
        long total = result.getTotal();
        List<ReviewPageResultBO> boList = result.getRecords();
        List<UserSelfReviewPageResultVO> voList = new ArrayList<>();
        for (ReviewPageResultBO bo : boList) {
            UserSelfReviewPageResultVO vo = new UserSelfReviewPageResultVO();
            String image = bo.getImages();
            //转成单个字符串
            List<String> images = StrUtil.split(image,",");
            vo.setImages(images);
            BeanUtils.copyProperties(bo,vo);
            voList.add(vo);
        }
        return new PageResult<>(total,voList);
    }

    /**
     * 商家端分页查询自己的评价列表
     * @param merchantId 商家id
     * @param pageQueryDTO 分页参数
     * @return 返回值
     */
    @Override
    public PageResult<MerchantReviewPageResultVO> merchantPageResult(Long merchantId, ReviewPageQueryDTO pageQueryDTO) {
        Page<ReviewPageResultBO> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        ReviewPageQueryBO pageQueryBO = new ReviewPageQueryBO();
        pageQueryBO.setProductId(pageQueryDTO.getProductId());
        pageQueryBO.setRating(pageQueryBO.getRating());
        pageQueryBO.setMerchantId(merchantId);
        Page<ReviewPageResultBO> result = reviewMapper.pageQuery(page,pageQueryBO);
        long total = result.getTotal();
        List<ReviewPageResultBO> boList = result.getRecords();
        List<MerchantReviewPageResultVO> voList = new ArrayList<>();
        for (ReviewPageResultBO bo : boList) {
            MerchantReviewPageResultVO vo = new MerchantReviewPageResultVO();
            String image = bo.getImages();
            //转成单个字符串
            List<String> images = StrUtil.split(image,",");
            vo.setImages(images);
            BeanUtils.copyProperties(bo,vo);
            voList.add(vo);
        }
        return new PageResult<>(total,voList);
    }
}
