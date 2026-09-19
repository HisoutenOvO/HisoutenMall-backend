package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.dto.product.ProductAddDTO;
import cn.hisouten.mall.pojo.vo.product.ProductDetailVO;

public interface ProductService {
    /**
     * 用户端查询商品详情
     * @param productId 商品ID
     * @return 返回商品详情信息
     */
    ProductDetailVO detailQuery(Long productId);

    /**
     * 商家端新增商品
     * @param productAddDTO 商品内容
     */
    void addProduct(ProductAddDTO productAddDTO);
}
