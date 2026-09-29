package cn.hisouten.mall.user.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户地址列表查询返回值
 */
@Data
@NoArgsConstructor
public class UserAddressListVO {
    private Long id;

    private String receiveName;

    private String province;

    private String city;

    private String district;

    private String detail;

    private Integer isDefault;
}
