package cn.hisouten.mall.user.service.impl;

import cn.hisouten.mall.user.mapper.AdminProfileMapper;
import cn.hisouten.mall.user.pojo.entity.AdminProfile;
import cn.hisouten.mall.user.service.AdminProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminProfileServiceImpl implements AdminProfileService {
    private final AdminProfileMapper adminProfileMapper;
}
