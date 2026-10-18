package cn.hisouten.mall.user.pojo.vo.admin;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员查询详细信息返回值
 */
@Data
public class AdminProfileDetailVO {

    private Long id;

    private Long userId;

    private String username;

    private String realName;

    private String department;

    private Integer adminLevel;

    private LocalDateTime createTIme;

    private LocalDateTime updateTime;
}
