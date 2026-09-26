package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.mapper.BrandMapper;
import cn.hisouten.mall.pojo.entity.Brand;
import cn.hisouten.mall.pojo.vo.brand.BrandListVO;
import cn.hisouten.mall.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 获取品牌列表
     * @return 返回值
     */
    @Override
    public List<BrandListVO> listQuery() {
        List<Brand> brandList = brandMapper.selectList(null);
        List<BrandListVO> brandVOList = new ArrayList<>();
        for (Brand brand : brandList) {
            BrandListVO brandListVO = new BrandListVO();
            brandListVO.setId(brand.getId());
            brandListVO.setName(brand.getName());
            brandListVO.setLogo(brand.getLogo());
            brandListVO.setSort(brand.getSort());
            brandVOList.add(brandListVO);
        }
        return brandVOList;
    }
}
