package cn.hisouten.mall.user.pojo.vo.user;

import lombok.Data;

/**
 * 商家查询店铺详细信息
 */
@Data
public class UserMerchantDetailVO {

    private Long merchantId;

    private String shopName;

    private String shopLogo;

    private String shopDescription;

    private Long followerCount;

    private Long productCount;

    private Boolean isFollowed;
}
