package cn.hisouten.mall.user.mapper;

import cn.hisouten.mall.user.pojo.dto.AdminMerchantPageQueryDTO;
import cn.hisouten.mall.user.pojo.entity.MerchantProfile;
import cn.hisouten.mall.user.pojo.entity.User;
import cn.hisouten.mall.user.pojo.vo.merchant.AdminMerchantPageResultVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;

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
    String selectExistedShopNameExceptSelf(String shopName,Long merchantId);

    /**
     * 查询是否有重复的联系电话
     * @param contactPhone 注册的联系电话
     * @return 可能存在的联系电话
     */
    String selectExistedContactPhoneExceptSelf(String contactPhone,Long userId);

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

    /**
     * 通过商家id获取关注人数
     * @param merchantId 商家id
     * @return 返回值
     */
    @Select("select count(*) from user_merchant_follow where merchant_id = #{merchantId}")
    Long selectFollowerCount(Long merchantId);

    /**
     * 查询用户是否关注商家
     * @param userId 用户id
     * @param merchantId 商家id
     * @return 返回值
     */
    @Select("select count(*) from user_merchant_follow where merchant_id = #{merchantId} and user_id = #{useId}")
    int isFollowed(Long userId, Long merchantId);

    /**
     * 关注店铺
     * @param userId 用户id
     * @param merchantId 店铺id
     */
    @Insert("insert into user_merchant_follow set user_id = #{userId}, merchant_id = #{merchantId}, create_time = NOW()")
    void follow(Long userId, Long merchantId);

    /**
     * 取关店铺
     * @param userId 用户id
     * @param merchantId 商家id
     */
    @Delete("delete from user_merchant_follow where user_id = #{userId} and merchant_id = #{merchantId}")
    void cancelFollow(Long userId, Long merchantId);
}
