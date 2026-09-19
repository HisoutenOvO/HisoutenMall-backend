package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.dto.product.ProductAddDTO;
import cn.hisouten.mall.pojo.dto.product.ProductUpdateDTO;
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

    /**
     * 修改商品
     * @param productId 需要修改商品的id
     * @param productUpdateDTO 修改的数据
     */
    void updateProduct(Long productId, ProductUpdateDTO productUpdateDTO);

    /**
     * 修改商品状态
     * @param productId 需要修改商品的id
     */
    void changeStatus(Long productId, Integer status);

    /**
     * 逻辑删除商品
     * @param productId 商品id
     */
    void logicDeleteProduct(Long productId);

    /**
     * 复原逻辑删除的商品
     * @param productId
     */
    void recoveryProduct(Long productId);
}
