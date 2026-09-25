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
public class AdminProfile{
    private Long id;

    private Long userId;

    private String realName;

    private String department;

    private Integer adminLevel;

    private LocalDateTime createTIme;

    private LocalDateTime updateTime;
}
