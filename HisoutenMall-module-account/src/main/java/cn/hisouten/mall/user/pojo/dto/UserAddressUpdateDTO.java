package cn.hisouten.mall.user.pojo.dto;

import lombok.Data;

/**
 * 用户修改地址参数
 */
@Data
public class UserAddressUpdateDTO {
    private String receiverName;

    private String receiverPhone;

    private String province;

    private String city;

    private String district;

    private String detail;

    private Integer isDefault;
}
