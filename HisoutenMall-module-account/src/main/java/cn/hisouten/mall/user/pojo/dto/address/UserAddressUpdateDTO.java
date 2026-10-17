package cn.hisouten.mall.user.pojo.dto.address;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户修改地址参数
 */
@Data
public class UserAddressUpdateDTO {

    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 50, message = "收货人姓名不能超过50字")
    private String receiverName;

    @NotBlank(message = "收货人手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String receiverPhone;

    @NotBlank(message = "省份不能为空")
    @Size(max = 50, message = "省份名称过长")
    private String province;

    @NotBlank(message = "城市不能为空")
    @Size(max = 50, message = "城市名称过长")
    private String city;

    @NotBlank(message = "区/县不能为空")
    @Size(max = 50, message = "区/县名称过长")
    private String district;

    @NotBlank(message = "详细地址不能为空")
    @Size(max = 255, message = "详细地址不能超过255字")
    private String detail;

    @NotNull(message = "是否默认不能为空")
    @Min(value = 0, message = "是否默认参数不合法")
    @Max(value = 1, message = "是否默认参数不合法")
    private Integer isDefault;

}