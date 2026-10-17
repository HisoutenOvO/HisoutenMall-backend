package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.Coupon;
import cn.hisouten.mall.pojo.entity.UserCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
