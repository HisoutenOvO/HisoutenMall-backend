package cn.hisouten.mall.user.pojo.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 *  商家修改信息参数
 */
@Data
public class MerchantUpdateDTO {

    @NotBlank(message = "店铺名称不能为空")
    @Size(max = 100, message = "店铺名称不能超过100字")
    private String shopName;

    @Size(max = 500, message = "店铺Logo链接不能超过500字")
    private String shopLogo;

    @Size(max = 500, message = "店铺简介不能超过500字")
    private String shopDescription;

    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确")
    private String contactPhone;
}