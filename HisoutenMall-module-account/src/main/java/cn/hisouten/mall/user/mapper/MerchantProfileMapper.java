package cn.hisouten.mall.user.mapper;

import cn.hisouten.mall.user.pojo.dto.AdminMerchantPageQueryDTO;
import cn.hisouten.mall.user.pojo.entity.MerchantProfile;
import cn.hisouten.mall.user.pojo.vo.AdminMerchantDetailVO;
import cn.hisouten.mall.user.pojo.vo.AdminMerchantPageResultVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MerchantProfileMapper extends BaseMapper<MerchantProfile> {

    /**
     * 通过商家id获取商家名称
     * @param merchantId 商家id
     * @return 商家名称
     */
    @Select("select shop_name from merchant_profile where user_id = #{merchantId}")
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

    /**
     * 分页查询商家
     * @param page 分页参数
     * @param adminMerchantPageQueryDTO 条件参数
     * @return 返回值
     */
    Page<AdminMerchantPageResultVO> pageQuery(Page<AdminMerchantPageResultVO> page,@Param("dto") AdminMerchantPageQueryDTO adminMerchantPageQueryDTO);

    /**
     * 根据商家的userId查询商家详情
     * @param merchantId 商家id——对应商家user表主键
     * @return 返回值
     */
    @Select("select * from merchant_profile where user_id = #{merchantId}")
    MerchantProfile selectProfileByMerchantId(Long merchantId);
}
