package cn.hisouten.mall.user.pojo.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户新增地址参数
 */
@Data
public class UserAddressAddDTO {

    @NotBlank(message = "收货人名称不能为空")
    private String receiverName;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String receiverPhone;

    @NotBlank(message = "省份不能为空")
    @Size(max = 20, message = "收货人名称不能超过20个字符")
    private String province;

    @NotBlank(message = "市不能为空")
    private String city;

    @NotBlank(message = "区不能为空")
    private String district;

    @NotBlank(message = "详细地址不能为空")
    @Size(max = 100, message = "详细地址不能超过100个字符")
    private String detail;

    @NotNull(message = "是否为默认地址不能为空")
    private Integer isDefault;

}
