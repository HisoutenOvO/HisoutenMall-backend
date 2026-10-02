package cn.hisouten.mall.user.service;

import cn.hisouten.mall.user.pojo.dto.AdminLoginDTO;
import cn.hisouten.mall.user.pojo.dto.MerchantLoginDTO;
import cn.hisouten.mall.user.pojo.dto.MerchantRegisterDTO;
import cn.hisouten.mall.user.pojo.dto.UserLoginDTO;
import cn.hisouten.mall.user.pojo.dto.UserRegisterDTO;
import cn.hisouten.mall.user.pojo.vo.LoginVO;
import cn.hisouten.mall.user.pojo.vo.UserInfoVO;

public interface AuthService {
    /**
     * 商家登录
     * @param merchantLoginDTO 商家登录参数
     * @return 返回值
     */
    LoginVO merchantLogin(MerchantLoginDTO merchantLoginDTO);

    /**
     * 用户登录
     * @param userLoginDTO 用户登录参数
     * @return 返回值
     */
    LoginVO userLogin(UserLoginDTO userLoginDTO);

    /**
     * 管理员登录
     * @param adminLoginDTO 管理员登录参数
     * @return 返回值
     */
    LoginVO adminLogin(AdminLoginDTO adminLoginDTO);

    /**
     * 用户注册
     * @param userRegisterDTO 用户注册参数
     */
    void userRegister(UserRegisterDTO userRegisterDTO);

    /**
     * 商家注册
     * @param merchantRegisterDTO 商家注册参数
     */
    void merchantRegister(MerchantRegisterDTO merchantRegisterDTO);

    /**
     * 查询当前用户信息
     * @param userId 用户if
     * @return 返回值
     */
    UserInfoVO infoQuery(Long userId);
}
