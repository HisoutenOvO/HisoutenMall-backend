package cn.hisouten.mall.user.service;

import cn.hisouten.mall.user.pojo.vo.UserAddressListVO;

import java.util.List;

public interface UserAddressService {

    /**
     * 用户地址列表查询
     * @param userId 用户id
     * @return 返回值
     */
    List<UserAddressListVO> listQuery(Long userId);
}
