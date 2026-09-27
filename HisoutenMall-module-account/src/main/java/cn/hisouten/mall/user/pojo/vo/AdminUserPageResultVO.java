package cn.hisouten.mall.user.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 管理员用户分页查询返回值
 */
@Data
@NoArgsConstructor
public class AdminUserPageResultVO {

    private Long id;

    private String username;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String nickname;

    private String avatar;

    private String phone;

    private String email;

    private Integer gender;

    private LocalDate birthday;

}
