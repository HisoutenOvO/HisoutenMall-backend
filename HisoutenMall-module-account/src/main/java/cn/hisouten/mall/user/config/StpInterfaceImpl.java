package cn.hisouten.mall.user.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.hisouten.mall.user.mapper.AuthMapper;
import cn.hisouten.mall.user.pojo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static cn.hisouten.mall.exception.constant.RoleConstant.*;

/**
 * 角色/权限数据源
 */
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {
    private final AuthMapper authMapper;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return Collections.emptyList();   // 不做权限点，返回空
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = Long.valueOf(loginId.toString());
        User user = authMapper.selectById(userId);
        if (user == null) return Collections.emptyList();
        // role 数字转字符串：1-user, 2-merchant, 3-admin
        return List.of(switch (user.getRole()) {
            case USER_ROLE -> "user";
            case MERCHANT_ROLE -> "merchant";
            case ADMIN_ROLE -> "admin";
            default -> "";
        });
    }
}