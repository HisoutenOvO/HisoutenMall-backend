package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.mapper.BrandMapper;
import cn.hisouten.mall.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandMapper brandMapper;

    /**
     * 通过品牌id获取品牌名称
     * @param brandId 品牌id
     * @return 品牌名称
     */
    @Override
    public String getBrandNameByBrandId(Long brandId) {
        return brandMapper.getBrandNameByBrandId(brandId);
    }
}
