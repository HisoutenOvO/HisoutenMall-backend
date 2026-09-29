package cn.hisouten.mall.user.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员查询商家详情返回值
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminMerchantDetailVO {
    private Long id;

    private String username;

    private Integer status;

    private String shopName;

    private String shopLogo;

    private String shopDescription;

    private String contactPhone;

    private String businessLicense;

    private Integer auditStatus;

    private String auditReason;
}
