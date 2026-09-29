package cn.hisouten.mall.user.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户查询单个地址详情
 */
@Data
@NoArgsConstructor
public class UserAddressDetailVO {

    private String receiverName;

    private String receiverPhone;

    private String province;

    private String city;

    private String district;

    private String detail;

    private Integer isDefault;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
