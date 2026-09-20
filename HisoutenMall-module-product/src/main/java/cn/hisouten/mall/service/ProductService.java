package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.product.MerchantProductAddDTO;
import cn.hisouten.mall.pojo.dto.product.ProductPageQueryDTO;
import cn.hisouten.mall.pojo.dto.product.MerchantProductUpdateDTO;
import cn.hisouten.mall.pojo.vo.product.MerchantProductDetailVO;
import cn.hisouten.mall.pojo.vo.product.MerchantProductListVO;
import cn.hisouten.mall.pojo.vo.product.UserProductDetailVO;
import cn.hisouten.mall.pojo.vo.product.UserProductListVO;

public interface ProductService {
    /**
     * 用户端查询商品详情
     * @param productId 商品ID
     * @return 返回商品详情信息
     */
    UserProductDetailVO userDetailQuery(Long productId);

    /**
     * 商家端查询商品详情
     * @param productId 商品ID
     * @return 返回商品详情信息
     */
    MerchantProductDetailVO merchantDetailQuery(Long productId);

    /**
     * 商家端新增商品
     * @param merchantProductAddDTO 商品内容
     */
    void addProduct(MerchantProductAddDTO merchantProductAddDTO);

    /**
     * 修改商品
     * @param productId 需要修改商品的id
     * @param merchantProductUpdateDTO 修改的数据
     */
    void updateProduct(Long productId, MerchantProductUpdateDTO merchantProductUpdateDTO);

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
     * 商家端分页查询商品分类
     * @param productPageQueryDTO 分页查询参数
     * @return 返回分页查询结果
     */
    PageResult<MerchantProductListVO> merchantPageQuery(ProductPageQueryDTO productPageQueryDTO);

    /**
     * 用户端分页查询商品分类
     * @param productPageQueryDTO 分页查询参数
     * @return 返回分页查询结果
     */
    PageResult<UserProductListVO> userPageQuery(ProductPageQueryDTO productPageQueryDTO);
}
