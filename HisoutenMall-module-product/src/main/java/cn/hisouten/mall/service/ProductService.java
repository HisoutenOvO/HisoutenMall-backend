package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.vo.product.ProductDetailVO;

public interface ProductService {
    /**
     * 查询商品详情
     * @param productId 商品ID
     * @return 返回商品详情信息
     */
    ProductDetailVO detailQuery(Long productId);
}
