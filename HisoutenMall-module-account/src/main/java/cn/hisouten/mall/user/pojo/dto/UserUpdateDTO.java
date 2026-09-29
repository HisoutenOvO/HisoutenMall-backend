package cn.hisouten.mall.user.pojo.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 用户修改个人信息参数
 */
@Data
public class UserUpdateDTO {

    private String nickname;

    private String avatar;

    private String phone;

    private String email;

    private Integer gender;

    private LocalDate birthday;
}
