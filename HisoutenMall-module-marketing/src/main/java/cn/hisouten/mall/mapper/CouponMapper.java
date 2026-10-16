package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.dto.AvailableCouponPageQueryDTO;
import cn.hisouten.mall.pojo.dto.MerchantCouponPageQueryDTO;
import cn.hisouten.mall.pojo.entity.Coupon;
import cn.hisouten.mall.pojo.vo.AvailableCouponPageResultVO;
import cn.hisouten.mall.pojo.vo.MerchantCouponPageResultVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 分页查询优惠券
     * @param merchantId 商家id
     * @param page 分页参数
     * @param pageQueryDTO 查询参数
     * @return 返回值
     */
    Page<MerchantCouponPageResultVO> pageQuery(Long merchantId, Page<MerchantCouponPageResultVO> page, MerchantCouponPageQueryDTO pageQueryDTO);

    /**
     * 分页查询可领取优惠券
     * @param page 分页参数
     * @param pageQueryDTO 查询参数
     * @return 返回值
     */
    Page<AvailableCouponPageResultVO> availablePageQuery(Page<AvailableCouponPageResultVO> page, AvailableCouponPageQueryDTO pageQueryDTO);
}
