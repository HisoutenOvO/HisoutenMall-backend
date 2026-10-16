package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.UserCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserCouponMapper extends BaseMapper<UserCoupon> {
    /**
     * 根据优惠券id查询优惠券
     * @param couponId 优惠券id
     * @return 返回值
     */
    @Select("select * from user_coupon where coupon_id = #{couponId}")
    UserCoupon selectByCouponId(Long couponId);
}
