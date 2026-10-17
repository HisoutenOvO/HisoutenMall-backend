package cn.hisouten.mall.user.pojo.dto.address;

import lombok.Data;

/**
 * 用户新增地址参数
 */
@Data
public class UserAddressAddDTO {

    private String receiverName;

    private String receiverPhone;

    private String province;

    private String city;

    private String district;

    private String detail;

    private Integer isDefault;

}
