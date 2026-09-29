package cn.hisouten.mall.user.service;

import cn.hisouten.mall.user.pojo.dto.UserAddressAddDTO;
import cn.hisouten.mall.user.pojo.vo.UserAddressDetailVO;
import cn.hisouten.mall.user.pojo.vo.UserAddressListVO;

import java.util.List;

public interface UserAddressService {

    /**
     * 用户地址列表查询
     * @param userId 用户id
     * @return 返回值
     */
    List<UserAddressListVO> listQuery(Long userId);

    /**
     * 用户查询地址详情
     * @param addressId 地址id
     * @return 返回值
     */
    UserAddressDetailVO detailQuery(Long addressId);

    /**
     * 用户新增地址
     * @param userId 用户id
     * @param userAddressAddDTO 用户新增地址参数
     */
    void addAddress(Long userId, UserAddressAddDTO userAddressAddDTO);
}
