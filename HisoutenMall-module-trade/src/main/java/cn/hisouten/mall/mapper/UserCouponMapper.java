package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.UserCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserCouponMapper extends BaseMapper<UserCoupon> {
    /**
     * 根据优惠券id查询优惠券
     * @param couponId 优惠券id
     * @return 返回值
     */
    @Select("select * from user_coupon where coupon_id = #{couponId}")
    UserCoupon selectByCouponId(Long couponId);

    /**
     * 根据用户id查询已有优惠券列表
     * @param userId 用户id
     * @return 返回值
     */
    @Select("select * from user_coupon where user_id = #{userId}")
    List<UserCoupon> selectListByUserId(Long userId);

    /**
     * 通过用户id和优惠券id查询优惠券
     * @param userId 用户id
     * @param couponId 优惠券id
     * @return 返回值
     */
    @Select("select * from user_coupon where coupon_id = #{couponId} and user_id = #{userId}")
    UserCoupon selectByCouponIdAndUserId(Long userId, Long couponId);

    /**
     * 根据订单id查询优惠券
     * @param orderId 订单id
     * @return 返回值
     */
    @Select("select * from user_coupon where order_id = #{orderId}")
    UserCoupon selectByOrderId(Long orderId);

    /**
     * 使用优惠券
     * @param id 用户优惠券id
     * @param orderId 订单id
     * @return 返回值
     */
    @Update("update user_coupon set status = 1, order_id = #{orderId}, use_time = NOW() " +
            "where id = #{id} and status = 0")
    int useCoupon(Long id, Long orderId);

    /**
     * 根据用户id和用户优惠券id查询优惠券
     * @param userCouponId 用户优惠券id
     * @param userId 优惠券id
     * @return 返回值
     */
    @Select("select * from user_coupon where user_id = #{userId} and id = #{userCouponId}")
    UserCoupon selectByIdAndUserId(Long userCouponId, Long userId);
}
