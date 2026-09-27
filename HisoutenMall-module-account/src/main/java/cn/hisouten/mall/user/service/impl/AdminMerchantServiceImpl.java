package cn.hisouten.mall.user.service.impl;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.user.mapper.AuthMapper;
import cn.hisouten.mall.user.mapper.MerchantProfileMapper;
import cn.hisouten.mall.user.pojo.dto.AdminMerchantPageQueryDTO;
import cn.hisouten.mall.user.pojo.vo.AdminMerchantPageResultVO;
import cn.hisouten.mall.user.service.AdminMerchantService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.hisouten.mall.constant.StatusConstant.DISABLED;

@Service
@RequiredArgsConstructor
public class AdminMerchantServiceImpl implements AdminMerchantService {
    private final MerchantProfileMapper merchantProfileMapper;
    private final AuthMapper authMapper;

    /**
     * 商家分页查询
     * @param adminMerchantPageQueryDTO 分页查询参数
     * @return 返回值
     */
    @Override
    public PageResult<AdminMerchantPageResultVO> pageQuery(AdminMerchantPageQueryDTO adminMerchantPageQueryDTO) {
        Page<AdminMerchantPageResultVO> page = new Page<>(adminMerchantPageQueryDTO.getPage(),adminMerchantPageQueryDTO.getPageSize());
        if(adminMerchantPageQueryDTO.getDeleted() == null){
            adminMerchantPageQueryDTO.setDeleted(DISABLED);
        }
        Page<AdminMerchantPageResultVO> result = merchantProfileMapper.pageQuery(page,adminMerchantPageQueryDTO);
        long total = result.getTotal();
        List<AdminMerchantPageResultVO> records = result.getRecords();
        return new PageResult<>(total,records);
    }
}
