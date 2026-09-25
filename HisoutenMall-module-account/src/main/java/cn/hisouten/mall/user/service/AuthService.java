package cn.hisouten.mall.user.service;

import cn.hisouten.mall.user.pojo.dto.merchant.MerchantLoginDTO;
import cn.hisouten.mall.user.pojo.vo.LoginVO;

public interface AuthService {
    /**
     * 商家登录
     * @param merchantLoginDTO 商家登录参数
     * @return 返回值
     */
    LoginVO merchantLogin(MerchantLoginDTO merchantLoginDTO);
}
