package cn.hisouten.mall.user.pojo.vo;

import lombok.Data;

/**
 * 通用的登录返回值
 */
@Data
public class LoginVO {

    private Long userId;

    private String username;

    private String token;

    private Integer role;
}
