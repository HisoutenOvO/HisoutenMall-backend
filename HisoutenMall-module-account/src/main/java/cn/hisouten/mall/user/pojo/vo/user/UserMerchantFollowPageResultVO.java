package cn.hisouten.mall.user.pojo.vo.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户分页查询已关注商家列表返回值
 */
@Data
@NoArgsConstructor
public class UserMerchantFollowPageResultVO {

    private Long merchantId;

    private String shopName;

    private String shopLogo;

    private Integer followerCount;

    private Integer productCount;

    private LocalDateTime followTime;

}
