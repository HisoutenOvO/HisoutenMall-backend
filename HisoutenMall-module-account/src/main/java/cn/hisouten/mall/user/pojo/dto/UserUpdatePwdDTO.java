package cn.hisouten.mall.user.pojo.dto;

import lombok.Data;

/**
 * 用户修改密码
 */
@Data
public class UserUpdatePwdDTO {
    private String oldPwd;

    private String newPwd;
}
