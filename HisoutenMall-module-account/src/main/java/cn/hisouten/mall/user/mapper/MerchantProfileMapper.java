package cn.hisouten.mall.user.mapper;

import cn.hisouten.mall.user.pojo.entity.MerchantProfile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MerchantProfileMapper extends BaseMapper<MerchantProfile> {

    /**
     * 通过商家id获取商家名称
     * @param merchantId 商家id
     * @return 商家名称
     */
    @Select("select shop_name from merchant_profile where id = #{merchantId}")
    String getMerchantNameByMerchantId(Long merchantId);

    /**
     * 查询是否有重复的店名
     * @param shopName 注册的店名
     * @return 可能存在的店名
     */
    @Select("select shop_name from merchant_profile where shop_name = #{shopName}")
    String selectExistedShopName(String shopName);

    /**
     * 查询是否有重复的联系电话
     * @param contactPhone 注册的联系电话
     * @return 可能存在的联系电话
     */
    @Select("select contact_phone from merchant_profile where contact_phone = #{contactPhone}")
    String selectExistedContactPhone(String contactPhone);
}
