package cn.hisouten.mall.user.pojo.dto;

import lombok.Data;

/**
 * 管理员登录参数
 */
@Data
public class AdminLoginDTO {

    private String username;

    private String password;
}
