package cn.hisouten.mall.user.pojo.dto.user;

import lombok.Data;

/**
 * 用户注册参数
 */
@Data
public class UserRegisterDTO {
    private String username;

    private String password;

    private String nickname;
}
