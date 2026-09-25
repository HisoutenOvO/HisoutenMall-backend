package cn.hisouten.mall.user.pojo.dto.merchant;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商家登录参数
 */
@Data
@NoArgsConstructor
public class MerchantLoginDTO {

    private String username;

    private String password;

}
