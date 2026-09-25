package cn.hisouten.mall.user.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {
    private Long id;

    private Long userId;

    private String username;

    private String nickname;

    private String avatar;

    private String phone;

    private String email;

    private Integer gender;

    private LocalDateTime birthday;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
