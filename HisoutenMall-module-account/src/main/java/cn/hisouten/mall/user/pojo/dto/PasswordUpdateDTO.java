package cn.hisouten.mall.user.pojo.dto;

import lombok.Data;

/**
 * 用户和商家端修改密码参数
 */
@Data
public class PasswordUpdateDTO {
    private String oldPwd;

    private String newPwd;
}
