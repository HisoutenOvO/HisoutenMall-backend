package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.OrderItemMapper;
import cn.hisouten.mall.mapper.OrderMapper;
import cn.hisouten.mall.mapper.ReviewMapper;
import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.pojo.bo.review.ReviewPageQueryBO;
import cn.hisouten.mall.pojo.bo.review.ReviewPageResultBO;
import cn.hisouten.mall.pojo.dto.review.AdminReviewPageQueryDTO;
import cn.hisouten.mall.pojo.dto.review.UserReviewAddDTO;
import cn.hisouten.mall.pojo.dto.review.ReviewPageQueryDTO;
import cn.hisouten.mall.pojo.entity.*;
import cn.hisouten.mall.pojo.vo.review.*;
import cn.hisouten.mall.service.ProductService;
import cn.hisouten.mall.service.ReviewService;
import cn.hisouten.mall.user.pojo.entity.User;
import cn.hisouten.mall.user.pojo.entity.UserProfile;
import cn.hisouten.mall.user.service.AuthService;
import cn.hisouten.mall.user.service.MerchantProfileService;
import cn.hisouten.mall.user.service.UserProfileService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.hisouten.mall.constant.CommonConstant.DEFAULT_RATING;
import static cn.hisouten.mall.constant.CommonConstant.DEFAULT_REVIEW;
import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.RoleConstant.ADMIN_ROLE;
import static cn.hisouten.mall.constant.RoleConstant.MERCHANT_ROLE;
import static cn.hisouten.mall.constant.StatusConstant.COMPLETED;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewMapper reviewMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    private final ProductService productService;
    private final AuthService authService;
    private final MerchantProfileService merchantProfileService;
    private final UserProfileService userProfileService;

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
    public PageResult<UserReviewPageResultVO> userPageQuery(ReviewPageQueryDTO pageQueryDTO) {
        Page<ReviewPageResultBO> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        ReviewPageQueryBO pageQueryBO = new ReviewPageQueryBO();
        pageQueryBO.setProductId(pageQueryDTO.getProductId());
        pageQueryBO.setRating(pageQueryDTO.getRating());
        Page<ReviewPageResultBO> result = reviewMapper.pageQuery(page,pageQueryBO);
        long total = result.getTotal();
        List<ReviewPageResultBO> boList = result.getRecords();
        List<UserReviewPageResultVO> voList = new ArrayList<>();
        for (ReviewPageResultBO bo : boList) {
            UserReviewPageResultVO vo = new UserReviewPageResultVO();
            BeanUtils.copyProperties(bo,vo);
            String image = bo.getImages();
            //转成单个字符串
            List<String> images = StrUtil.split(image,",");
            vo.setImages(images);
            //脱敏处理
            vo.setNickname(StrUtil.hide(bo.getNickname(),1,bo.getNickname().length()));
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
    public PageResult<UserSelfReviewPageResultVO> selfPageQuery(Long userId, ReviewPageQueryDTO pageQueryDTO) {
        Page<ReviewPageResultBO> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        ReviewPageQueryBO pageQueryBO = new ReviewPageQueryBO();
        pageQueryBO.setProductId(pageQueryDTO.getProductId());
        pageQueryBO.setRating(pageQueryDTO.getRating());
        pageQueryBO.setUserId(userId);
        Page<ReviewPageResultBO> result = reviewMapper.pageQuery(page,pageQueryBO);
        long total = result.getTotal();
        List<ReviewPageResultBO> boList = result.getRecords();
        List<UserSelfReviewPageResultVO> voList = new ArrayList<>();
        for (ReviewPageResultBO bo : boList) {
            UserSelfReviewPageResultVO vo = new UserSelfReviewPageResultVO();
            BeanUtils.copyProperties(bo,vo);
            String image = bo.getImages();
            //转成单个字符串
            List<String> images = StrUtil.split(image,",");
            vo.setImages(images);
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
    public PageResult<MerchantReviewPageResultVO> merchantPageQuery(Long merchantId, ReviewPageQueryDTO pageQueryDTO) {
        Page<ReviewPageResultBO> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        ReviewPageQueryBO pageQueryBO = new ReviewPageQueryBO();
        pageQueryBO.setProductId(pageQueryDTO.getProductId());
        pageQueryBO.setRating(pageQueryDTO.getRating());
        pageQueryBO.setMerchantId(merchantId);
        Page<ReviewPageResultBO> result = reviewMapper.pageQuery(page,pageQueryBO);
        long total = result.getTotal();
        List<ReviewPageResultBO> boList = result.getRecords();
        List<MerchantReviewPageResultVO> voList = new ArrayList<>();
        for (ReviewPageResultBO bo : boList) {
            MerchantReviewPageResultVO vo = new MerchantReviewPageResultVO();
            BeanUtils.copyProperties(bo,vo);
            String image = bo.getImages();
            //转成单个字符串
            List<String> images = StrUtil.split(image,",");
            vo.setImages(images);
            voList.add(vo);
        }
        return new PageResult<>(total,voList);
    }

    /**
     * 用户删除评价
     * @param userId 用户id
     * @param reviewId 评价id
     */
    @Override
    public void deleteReview(Long userId, Long reviewId) {
        ProductReview review = reviewMapper.selectById(reviewId);
        if(review == null || !review.getUserId().equals(userId)){
            throw new BizException(REVIEW_NOT_FOUND);
        }
        //由于在新增评论的校验时，查询是否存在评价未设置deleted = 0，使得用户删除评价后也不允许再次评价避免刷评
        reviewMapper.deleteById(reviewId);
    }

    /**
     * 用户分页查询待评价商品
     * @param userId 用户id
     * @param pageQueryDTO 查询条件
     * @return 返回值
     */
    @Override
    public PageResult<UserPendingReviewPageResultVO> pendingPageQuery(Long userId, BasePageQueryDTO pageQueryDTO) {
        Page<UserPendingReviewPageResultVO> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        Page<UserPendingReviewPageResultVO> result = reviewMapper.pendingPageQuery(userId,page,pageQueryDTO);
        return new PageResult<>(result.getTotal(),result.getRecords());
    }

    /**
     * 商家端查询商品评价统计
     * @param merchantId 商家id
     * @param productId 商品id
     * @return 返回值
     */
    @Override
    public MerchantReviewStatisticsVO statisticsQuery(Long merchantId, Long productId) {
        Product product = productService.getProductByProductId(productId);
        if(product == null || !product.getMerchantId().equals(merchantId)){
            throw new BizException(PRODUCT_NOT_FOUND);
        }
        List<ProductReview> reviewList = reviewMapper.selectByProductId(productId);
        if(reviewList.isEmpty()){
            MerchantReviewStatisticsVO emptyVO = new MerchantReviewStatisticsVO();
            emptyVO.setTotalCount(0);
            emptyVO.setAvgRating(BigDecimal.ZERO);
            emptyVO.setGoodCount(0);
            emptyVO.setGoodRate(0);
            emptyVO.setRatingCountMap(null);
            return emptyVO;
        }
        int totalCount = reviewList.size();
        int sumRating = reviewList.stream().mapToInt(ProductReview::getRating).sum();
        BigDecimal avgRating = BigDecimal.valueOf(sumRating)
                .divide(BigDecimal.valueOf(totalCount), 1, RoundingMode.HALF_UP);
        int goodCount = (int) reviewList.stream().filter(r -> r.getRating() >= 4).count();
        int goodRate = goodCount * 100 / totalCount;
        Map<Integer,Integer> ratingCountMap = new HashMap<>();
        //初始化Map
        for(int i = 0; i <= 5; i++) ratingCountMap.put(i,0);
        for (ProductReview review : reviewList) {
            //对于每个星级，不存在就设为1，存在就累加
            ratingCountMap.merge(review.getRating(),1,Integer::sum);
        }
        MerchantReviewStatisticsVO vo = new MerchantReviewStatisticsVO();
        vo.setTotalCount(totalCount);
        vo.setAvgRating(avgRating);
        vo.setGoodCount(goodCount);
        vo.setGoodRate(goodRate);
        vo.setRatingCountMap(ratingCountMap);
        return vo;
    }

    /**
     * 管理端分页查询评价
     * @param adminId 管理员id
     * @param pageQueryDTO 分页查询条件
     * @return 返回值
     */
    @Override
    public PageResult<AdminReviewPageResultVO> adminPageQuery(Long adminId, AdminReviewPageQueryDTO pageQueryDTO) {
        Page<ReviewPageResultBO> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        ReviewPageQueryBO pageQueryBO = new ReviewPageQueryBO();
        pageQueryBO.setProductId(pageQueryDTO.getProductId());
        pageQueryBO.setRating(pageQueryDTO.getRating());
        pageQueryBO.setMerchantId(pageQueryDTO.getMerchantId());
        pageQueryBO.setMerchantName(pageQueryDTO.getMerchantName());
        Page<ReviewPageResultBO> result = reviewMapper.pageQuery(page,pageQueryBO);
        long total = result.getTotal();
        List<ReviewPageResultBO> boList = result.getRecords();
        List<AdminReviewPageResultVO> voList = new ArrayList<>();
        for (ReviewPageResultBO bo : boList) {
            AdminReviewPageResultVO vo = new AdminReviewPageResultVO();
            BeanUtils.copyProperties(bo,vo);
            String image = bo.getImages();
            //转成单个字符串
            List<String> images = StrUtil.split(image,",");
            vo.setImages(images);
            voList.add(vo);
        }
        return new PageResult<>(total,voList);
    }

    /**
     * 管理员强制删除评价
     * @param adminId 管理员id
     * @param reviewId 评价id
     */
    @Override
    public void adminDeleteReview(Long adminId, Long reviewId) {
        reviewMapper.deleteById(reviewId);
    }

    /**
     * 查询评价详情
     * @param userId 商家id
     * @param reviewId 评价id
     * @return 返回值
     */
    @Override
    public ReviewDetailVO detailQuery(Long userId, Long reviewId) {
        User user = authService.getUserByUserId(userId);
        if(user == null){
            throw new BizException(NO_PERMISSION);
        }
        ProductReview review = reviewMapper.selectById(reviewId);
        if(review == null){
            throw new BizException(REVIEW_NOT_FOUND);
        }
        if(user.getRole() == MERCHANT_ROLE) {
            Product product = productService.getProductByProductId(review.getProductId());
            if (product == null || !product.getMerchantId().equals(userId)) {
                throw new BizException(PRODUCT_NOT_FOUND);
            }
        }
        ReviewDetailVO detailVO = new ReviewDetailVO();
        BeanUtils.copyProperties(review,detailVO);
        UserProfile userProfile = userProfileService.getProfileByUserId(review.getUserId());
        if(userProfile != null){
            detailVO.setNickname(userProfile.getNickname());
            detailVO.setAvatar(userProfile.getAvatar());
        }
        Product product = productService.getProductByProductId(review.getProductId());
        if(product == null){
            throw new BizException(PRODUCT_NOT_FOUND);
        }
        detailVO.setProductName(product.getName());
        List<String> images = StrUtil.split(review.getImage(),",");
        detailVO.setImages(images);
        detailVO.setProductImage(product.getMainImage());
        Order order = orderMapper.selectById(review.getOrderId());
        if(order == null){
            throw new BizException(ORDER_NOT_FOUND);
        }
        detailVO.setOrderNo(order.getOrderNo());
        if(user.getRole() != MERCHANT_ROLE) {
            detailVO.setMerchantName(merchantProfileService.getMerchantNameByMerchantId(order.getMerchantId()));
        }
        return detailVO;
    }

    /**
     * 获取商家评价总数
     * @param merchantId 商家id
     * @return 返回值
     */
    @Override
    public Long getTotalReviewCount(Long merchantId) {
        return reviewMapper.selectTotalReviewCount(merchantId);
    }
}
