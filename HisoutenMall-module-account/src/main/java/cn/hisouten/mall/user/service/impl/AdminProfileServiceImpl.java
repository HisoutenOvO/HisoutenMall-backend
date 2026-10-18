package cn.hisouten.mall.user.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.user.mapper.AdminProfileMapper;
import cn.hisouten.mall.user.mapper.AuthMapper;
import cn.hisouten.mall.user.pojo.entity.AdminProfile;
import cn.hisouten.mall.user.pojo.entity.User;
import cn.hisouten.mall.user.pojo.vo.admin.AdminProfileDetailVO;
import cn.hisouten.mall.user.service.AdminProfileService;
import cn.hisouten.mall.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AdminProfileServiceImpl implements AdminProfileService {
    private final AdminProfileMapper adminProfileMapper;
    private final AuthMapper authMapper;

    /**
     * 管理员查询详细信息
     * @param adminId 管理员id
     * @return 返回值
     */
    @Override
    public AdminProfileDetailVO detailQuery(Long adminId) {
        AdminProfile adminProfile = adminProfileMapper.selectProfileByUserId(adminId);
        if(adminProfile == null){
            throw new BizException(USER_NOT_FOUND);
        }
        User user = authMapper.selectById(adminId);
        if(user == null){
            throw new BizException(USER_NOT_FOUND);
        }
        AdminProfileDetailVO detailVO = new AdminProfileDetailVO();
        BeanUtils.copyProperties(adminProfile,detailVO);
        detailVO.setUsername(user.getUsername());
        return detailVO;
    }
}
