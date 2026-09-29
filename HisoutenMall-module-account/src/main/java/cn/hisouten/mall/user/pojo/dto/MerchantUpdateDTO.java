package cn.hisouten.mall.user.pojo.dto;

import lombok.Data;

/**
 *  商家修改信息参数
 */
@Data
public class MerchantUpdateDTO {
    private String shopName;

    private String shopLogo;

    private String shopDescription;

    private String contactPhone;
}
