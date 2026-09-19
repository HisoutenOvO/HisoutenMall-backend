package cn.hisouten.mall.service;

public interface BrandService {

    /**
     * 通过品牌id获取品牌名称
     * @param brandId 品牌id
     * @return 品牌名称
     */
    String getBrandNameByBrandId(Long brandId);
}
