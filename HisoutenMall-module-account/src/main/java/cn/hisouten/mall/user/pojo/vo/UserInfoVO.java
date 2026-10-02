package cn.hisouten.mall.user.pojo.vo;

import lombok.Data;

/**
 * 通用的查询当前用户信息返回值
 */
@Data
public class UserInfoVO {
    // 通用
    private Long userId;
    private String username;
    private Integer role;

    // 用户专有
    private String nickname;
    private String avatar;
    private String phone;
    private String email;

    // 商家专有
    private String shopName;
    private String shopLogo;
    private Integer auditStatus;

    // 管理员专有
    private String realName;
    private Integer adminLevel;
}
