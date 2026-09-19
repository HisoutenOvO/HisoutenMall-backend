package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.merchant.product.ProductAddDTO;
import cn.hisouten.mall.pojo.dto.common.ProductPageQueryDTO;
import cn.hisouten.mall.pojo.dto.merchant.product.ProductUpdateDTO;
import cn.hisouten.mall.pojo.vo.product.ProductDetailVO;
import cn.hisouten.mall.pojo.vo.product.ProductListVO;

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
     * @param productId 商品id
     */
    void recoveryProduct(Long productId);

    /**
     * 彻底删除商品数据
     * @param productId 商品id
     */
    void deleteProduct(Long productId);

    /**
     * 分页查询商品分类
     * @param productPageQueryDTO 分页查询参数
     * @return 返回分页查询结果
     */
    PageResult<ProductListVO> pageQuery(ProductPageQueryDTO productPageQueryDTO);
}
