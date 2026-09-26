package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.product.*;
import cn.hisouten.mall.pojo.vo.product.*;

import java.util.List;

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
     * @param merchantProductPageQueryDTO 分页查询参数
     * @return 返回分页查询结果
     */
    PageResult<MerchantProductPageResultVO> merchantPageQuery(MerchantProductPageQueryDTO merchantProductPageQueryDTO);

    /**
     * 用户端分页查询商品分类
     * @param userProductPageQueryDTO 分页查询参数
     * @return 返回分页查询结果
     */
    PageResult<UserProductPageResultVO> userPageQuery(UserProductPageQueryDTO userProductPageQueryDTO);

    /**
     * 查询某个商品全部sku
     * @param productId 查询sku的商品id
     * @return 返回值
     */
    List<MerchantProductSkuVO> listQuerySku(Long productId);

    /**
     * 查询某个sku详情
     * @param skuId skuId
     * @return 返回值
     */
    MerchantProductSkuVO skuDetailQuery(Long skuId);

    /**
     * 修改sku
     * @param skuId skuId
     * @param merchantProductSkuDTO 修改参数
     */
    void updateSku(Long skuId, MerchantProductSkuDTO merchantProductSkuDTO);

    /**
     * 修改sku上下架状态
     * @param skuId skuId
     * @param status 状态
     * @return 返回值
     */
    void changeSkuStatus(Long skuId, Integer status);

    /**
     * 逻辑删除sku
     * @param skuId skuId
     */
    void logicDeleteSku(Long skuId);

    /**
     * 恢复删除掉的sku
     * @param skuId skuId
     */
    void recoverySku(Long skuId);

    /**
     * 彻底删除sku
     * @param skuId skuId
     */
    void deleteSku(Long skuId);

}
