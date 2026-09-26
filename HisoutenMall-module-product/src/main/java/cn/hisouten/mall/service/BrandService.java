package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.vo.brand.BrandListVO;

import java.util.List;

public interface BrandService {

    /**
     * 通过品牌id获取品牌名称
     * @param brandId 品牌id
     * @return 品牌名称
     */
    String getBrandNameByBrandId(Long brandId);

    /**
     * 获取品牌列表
     * @return 返回值
     */
    List<BrandListVO> listQuery();
}
