package cn.hisouten.mall.user.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.exception.businessexception.*;
import cn.hisouten.mall.user.mapper.AuthMapper;
import cn.hisouten.mall.user.mapper.MerchantProfileMapper;
import cn.hisouten.mall.user.mapper.UserProfileMapper;
import cn.hisouten.mall.user.pojo.dto.admin.AdminLoginDTO;
import cn.hisouten.mall.user.pojo.dto.merchant.MerchantLoginDTO;
import cn.hisouten.mall.user.pojo.dto.merchant.MerchantRegisterDTO;
import cn.hisouten.mall.user.pojo.dto.user.UserLoginDTO;
import cn.hisouten.mall.user.pojo.dto.user.UserRegisterDTO;
import cn.hisouten.mall.user.pojo.entity.MerchantProfile;
import cn.hisouten.mall.user.pojo.entity.User;
import cn.hisouten.mall.user.pojo.entity.UserProfile;
import cn.hisouten.mall.user.pojo.vo.LoginVO;
import cn.hisouten.mall.user.service.AuthService;
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cn.hisouten.mall.exception.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.exception.constant.RoleConstant.*;
import static cn.hisouten.mall.exception.constant.StatusConstant.ENABLED;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthMapper authMapper;
    private final UserProfileMapper userProfileMapper;
    private final MerchantProfileMapper merchantProfileMapper;

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
     * 商家注册
     * @param merchantRegisterDTO 商家注册参数
     */
    @Override
    @Transactional
    public void merchantRegister(MerchantRegisterDTO merchantRegisterDTO) {
        //查询是否重名
        String existedUsername = authMapper.selectExistedUserName(merchantRegisterDTO.getUsername());
        if(existedUsername != null){
            throw new UserAlreadyExistException(USER_ALREADY_EXIST);
        }

        //插入user表
        User user = new User();
        user.setUsername(merchantRegisterDTO.getUsername());
        user.setPassword(BCrypt.hashpw(merchantRegisterDTO.getPassword()));
        user.setRole(MERCHANT_ROLE);
        user.setStatus(ENABLED);
        authMapper.insert(user);

        //插入merchant_profile表
        //查询店名和联系电话是否重复
        String existedShopName = merchantProfileMapper.selectExistedShopName(merchantRegisterDTO.getShopName());
        if(existedShopName != null){
            throw new ShopNameAlreadyExistException(SHOP_NAME_ALREADY_EXIST);
        }
        String existedContactPhone = merchantProfileMapper.selectExistedContactPhone(merchantRegisterDTO.getContactPhone());
        if(existedContactPhone != null){
            throw new ContactPhoneAlreadyExistException(CONTACT_PHONE_ALREADY_EXIST);
        }
        MerchantProfile profile = new MerchantProfile();
        profile.setUserId(user.getId());
        profile.setShopName(merchantRegisterDTO.getShopName());
        profile.setContactPhone(merchantRegisterDTO.getContactPhone());
        profile.setAuditStatus(ENABLED); //默认通过，后期改成管理员审核
        merchantProfileMapper.insert(profile);
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
     * 用户注册
     * @param userRegisterDTO 用户注册参数
     */
    @Override
    @Transactional
    public void userRegister(UserRegisterDTO userRegisterDTO) {
        //查询是否重名
        String existedUsername = authMapper.selectExistedUserName(userRegisterDTO.getUsername());
        if(existedUsername != null){
            throw new UserAlreadyExistException(USER_ALREADY_EXIST);
        }
        //插入user表
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(BCrypt.hashpw(userRegisterDTO.getPassword()));
        user.setRole(USER_ROLE);
        user.setStatus(ENABLED);
        authMapper.insert(user);
        //插入user_profile表
        UserProfile profile = new UserProfile();
        profile.setUserId(user.getId());
        profile.setNickname(userRegisterDTO.getNickname());
        userProfileMapper.insert(profile);
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
