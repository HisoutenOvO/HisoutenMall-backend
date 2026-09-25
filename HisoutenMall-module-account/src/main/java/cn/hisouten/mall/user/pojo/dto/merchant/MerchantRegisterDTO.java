package cn.hisouten.mall.user.pojo.dto.merchant;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商家注册参数
 */
@Data
@NoArgsConstructor
public class MerchantRegisterDTO {
    private String username;

    private String password;

    private String shopName;

    private String contactPhone;
}
