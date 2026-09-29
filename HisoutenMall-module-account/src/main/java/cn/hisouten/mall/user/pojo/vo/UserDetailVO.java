package cn.hisouten.mall.user.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 用户在个人中心查看自身详情返回值
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailVO {
    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private String phone;

    private String email;

    private Integer gender;

    private LocalDate birthday;


}
