package cn.hisouten.mall.user.service.impl;

import cn.hisouten.mall.user.mapper.UserAddressMapper;
import cn.hisouten.mall.user.pojo.entity.UserAddress;
import cn.hisouten.mall.user.pojo.vo.UserAddressListVO;
import cn.hisouten.mall.user.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {
    private final UserAddressMapper userAddressMapper;


    /**
     * 用户地址列表查询
     * @param userId 用户id
     * @return 返回值
     */
    @Override
    public List<UserAddressListVO> listQuery(Long userId) {
        List<UserAddress> userAddressList = userAddressMapper.selectListByUserId(userId);
        List<UserAddressListVO> userAddressListVOS = new ArrayList<>();
        for (UserAddress userAddress : userAddressList) {
            UserAddressListVO vo = new UserAddressListVO();
            vo.setId(userAddress.getId());
            vo.setReceiveName(userAddress.getReceiverName());
            vo.setProvince(userAddress.getProvince());
            vo.setCity(userAddress.getCity());
            vo.setDistrict(userAddress.getDistrict());
            vo.setDetail(userAddress.getDetail());
            vo.setIsDefault(userAddress.getIsDefault());
            userAddressListVOS.add(vo);
        }
        return userAddressListVOS;
    }
}
