package cn.hisouten.mall.user.service;

import cn.hisouten.mall.user.pojo.dto.login.AdminLoginDTO;
import cn.hisouten.mall.user.pojo.dto.login.MerchantLoginDTO;
import cn.hisouten.mall.user.pojo.dto.login.UserLoginDTO;
import cn.hisouten.mall.user.pojo.dto.register.MerchantRegisterDTO;
import cn.hisouten.mall.user.pojo.dto.register.UserRegisterDTO;
import cn.hisouten.mall.user.pojo.dto.update.PasswordUpdateDTO;
import cn.hisouten.mall.user.pojo.entity.User;
import cn.hisouten.mall.user.pojo.vo.LoginVO;
import cn.hisouten.mall.user.pojo.vo.user.UserInfoVO;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 修改密码
     * @param userId 用户id
     * @param passwordUpdateDTO 密码
     */
    void updatePwd(Long userId, PasswordUpdateDTO passwordUpdateDTO);

    /**
     * 根据用户id查询用户
     * @param userId 用户id
     * @return 返回值
     */
    User getUserByUserId(Long userId);

    /**
     * 获取总用户数
     * @return 返回值
     */
    Long getTotalUserCount();


    /**
     * 获取总商家数
     * @return 返回值
     */
    Long getTotalMerchantCount();

    /**
     * 获取待审核商家数
     * @return
     */
    Long getPendingMerchantAudit();
}
