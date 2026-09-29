package cn.hisouten.mall.user.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商家查询店铺详情返回值
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantDetailVO {
    private Long id;

    private String username;

    private String shopName;

    private String shopLogo;

    private String shopDescription;

    private String contactPhone;

    private String businessLicense;

    private Integer auditStatus;

    private String auditReason;
}
