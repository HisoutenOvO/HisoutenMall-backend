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
}
