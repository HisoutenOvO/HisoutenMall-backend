package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.Coupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CouponMapper extends BaseMapper<Coupon> {

    /**
     * 根据优惠券名查找优惠券
     * @param merchantId 商家id
     * @param name 名称
     * @return 返回值
     */
    @Select("select * from coupon where merchant_id = #{merchantId} and name = #{name} and deleted = 0")
    Coupon selectByName(Long merchantId, String name);
}
