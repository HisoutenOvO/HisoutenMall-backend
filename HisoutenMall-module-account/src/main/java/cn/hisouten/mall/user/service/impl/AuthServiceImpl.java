package cn.hisouten.mall.user.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.exception.businessexception.UserNotMatchException;
import cn.hisouten.mall.exception.businessexception.UserStatusErrorException;
import cn.hisouten.mall.user.mapper.AuthMapper;
import cn.hisouten.mall.user.pojo.dto.admin.AdminLoginDTO;
import cn.hisouten.mall.user.pojo.dto.merchant.MerchantLoginDTO;
import cn.hisouten.mall.user.pojo.dto.user.UserLoginDTO;
import cn.hisouten.mall.user.pojo.entity.User;
import cn.hisouten.mall.user.pojo.vo.LoginVO;
import cn.hisouten.mall.user.service.AuthService;
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static cn.hisouten.mall.exception.constant.ExceptionMessageConstant.USER_NOT_MATCH;
import static cn.hisouten.mall.exception.constant.ExceptionMessageConstant.USER_STATUS_ERROR;
import static cn.hisouten.mall.exception.constant.RoleConstant.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthMapper authMapper;

    /**
     * 商家登录
     * @param merchantLoginDTO 商家登录参数
     * @return 返回值
     */
    @Override
    public LoginVO merchantLogin(MerchantLoginDTO merchantLoginDTO) {
        String username = merchantLoginDTO.getUsername();
        String password = merchantLoginDTO.getPassword();
        //调用内部统一登录方法
        return doLogin(username,password,MERCHANT_ROLE);
    }

    /**
     * 用户登录
     * @param userLoginDTO 用户登录参数
     * @return 返回值
     */
    @Override
    public LoginVO userLogin(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        return doLogin(username,password,USER_ROLE);
    }

    /**
     * 管理员登录
     * @param adminLoginDTO 管理员登录参数
     * @return 返回值
     */
    @Override
    public LoginVO adminLogin(AdminLoginDTO adminLoginDTO) {
        String username = adminLoginDTO.getUsername();
        String password = adminLoginDTO.getPassword();
        return doLogin(username,password,ADMIN_ROLE);
    }

    /**
     * 统一登录方法抽离
     * @param username 用户名
     * @param password 密码
     * @param expectedRole 实际应该匹配的角色
     * @return 返回LoginVO
     */
    private LoginVO doLogin(String username,String password,int expectedRole){
        //先获取到对象
        User user = authMapper.getUserByUserName(username);
        //判断非空以及角色匹配
        if(user == null || user.getRole() != expectedRole){
            throw new UserNotMatchException(USER_NOT_MATCH);
        }
        //再检查状态是否正常
        if(user.getStatus() != 1){
            throw new UserStatusErrorException(USER_STATUS_ERROR);
        }
        //匹配密码
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new UserNotMatchException(USER_NOT_MATCH);
        }
        //登录
        StpUtil.login(user.getId());
        //返回必要信息
        LoginVO vo = new LoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setToken(StpUtil.getTokenValue());
        vo.setRole(user.getRole());
        return vo;
    }
}
