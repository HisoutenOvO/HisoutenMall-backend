package cn.hisouten.mall.user.service;

import cn.hisouten.mall.user.pojo.dto.admin.AdminLoginDTO;
import cn.hisouten.mall.user.pojo.dto.merchant.MerchantLoginDTO;
import cn.hisouten.mall.user.pojo.dto.user.UserLoginDTO;
import cn.hisouten.mall.user.pojo.vo.LoginVO;

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
}
