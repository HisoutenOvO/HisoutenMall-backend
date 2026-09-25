package cn.hisouten.mall.user.pojo.dto.user;

import lombok.Data;

/**
 * 用户登录参数
 */
@Data
public class UserLoginDTO {

    private String username;

    private String password;

}
