package cn.hisouten.mall.user.pojo.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * 用户修改个人信息参数
 */
@Data
public class UserUpdateDTO {

    @NotBlank(message = "昵称不能为空")
    @Size(max = 50, message = "昵称不能超过50字")
    private String nickname;

    @Size(max = 500, message = "头像链接不能超过500字")
    private String avatar;

    // 如果允许不改手机号传null，就保留，如果传了就必须符合格式
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱不能超过50字")
    private String email;

    @Min(value = 0, message = "性别参数不合法")
    @Max(value = 2, message = "性别参数不合法")
    private Integer gender;

    private LocalDate birthday;
}