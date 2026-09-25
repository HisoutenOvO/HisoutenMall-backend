package cn.hisouten.mall.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.exception.businessexception.*;
import cn.hisouten.mall.mapper.ProductMapper;
import cn.hisouten.mall.mapper.ProductSkuMapper;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.bo.product.ProductPageQueryBO;
import cn.hisouten.mall.pojo.dto.product.*;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.bo.product.ProductPageResultBO;
import cn.hisouten.mall.pojo.entity.ProductSku;
import cn.hisouten.mall.pojo.vo.product.MerchantProductDetailVO;
import cn.hisouten.mall.pojo.vo.product.MerchantProductPageResultVO;
import cn.hisouten.mall.pojo.vo.product.UserProductDetailVO;
import cn.hisouten.mall.pojo.vo.product.UserProductPageResultVO;
import cn.hisouten.mall.service.BrandService;
import cn.hisouten.mall.service.CategoryService;
import cn.hisouten.mall.service.ProductService;
import cn.hisouten.mall.user.service.MerchantProfileService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.StatusConstant.DISABLED;
import static cn.hisouten.mall.constant.StatusConstant.ENABLED;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    //注入自己的mapper
    private final ProductMapper productMapper;
    private final ProductSkuMapper productSkuMapper;
    //注入别人的service
    private final MerchantProfileService merchantProfileService;
    private final CategoryService categoryService;
    private final BrandService brandService;


    /**
     * 商家端查询商品详情
     * @param productId 商品ID
     * @return 返回商品详情信息
     */
    @Override
    public MerchantProductDetailVO merchantDetailQuery(Long productId) {
        Product product = productMapper.selectById(productId);
        //如果商品不存在或已被逻辑删除，抛出异常
        if(product == null){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        //权限审查
        if(!product.getMerchantId().equals(StpUtil.getLoginIdAsLong())){
            throw new NoPermissionException(NO_PERMISSION);
        }
        MerchantProductDetailVO merchantProductDetailVO = new MerchantProductDetailVO();
        BeanUtils.copyProperties(product, merchantProductDetailVO);
        String categoryName = categoryService.getCategoryNameByCategoryId(product.getCategoryId());
        String merchantName = merchantProfileService.getMerchantNameByMerchantId(product.getMerchantId());
        String brandName = brandService.getBrandNameByBrandId(product.getBrandId());
        merchantProductDetailVO.setBrandName(brandName);
        merchantProductDetailVO.setCategoryName(categoryName);
        merchantProductDetailVO.setMerchantName(merchantName);

        return merchantProductDetailVO;
    }


    /**
     * 用户端查询商品详情
     * @param productId 商品ID
     * @return 返回商品详情信息
     */
    @Override
    public UserProductDetailVO userDetailQuery(Long productId) {
        Product product = productMapper.selectById(productId);
        //如果商品不存在或已被逻辑删除，抛出异常
        if(product == null){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        UserProductDetailVO userProductDetailVO = new UserProductDetailVO();
        BeanUtils.copyProperties(product, userProductDetailVO);
        String categoryName = categoryService.getCategoryNameByCategoryId(product.getCategoryId());
        String merchantName = merchantProfileService.getMerchantNameByMerchantId(product.getMerchantId());
        String brandName = brandService.getBrandNameByBrandId(product.getBrandId());
        userProductDetailVO.setBrandName(brandName);
        userProductDetailVO.setCategoryName(categoryName);
        userProductDetailVO.setMerchantName(merchantName);

        return userProductDetailVO;
    }


    /**
     * 商家端新增商品
     * @param merchantProductAddDTO 商品内容
     */
    @Override
    @Transactional
    public void addProduct(MerchantProductAddDTO merchantProductAddDTO) {
        Product product = new Product();
        //判断同一商家下商品是否同名
        String existedProductName = productMapper.selectExistedProductName(merchantProductAddDTO.getName(),StpUtil.getLoginIdAsLong());
        if(existedProductName != null){
            throw new ProductNameAlreadyExistException(PRODUCT_NAME_ALREADY_EXIST);
        }
        BeanUtils.copyProperties(merchantProductAddDTO,product);
        product.setMerchantId(StpUtil.getLoginIdAsLong());
        productMapper.insert(product);

        //增加新的sku
        List<ProductSku> productSkuList = new ArrayList<>();
        List<ProductSkuItemDTO> skuList = merchantProductAddDTO.getSkuList();
        //借用set集合的add实现去重逻辑——set中已存在的话再调用add方法就会返回false
        Set<String> specSet = new HashSet<>();
        for (ProductSkuItemDTO skuItem : skuList) {
            //检查specs，同一个产品不能有相同规格
            if (skuItem.getSpecs() != null && !specSet.add(skuItem.getSpecs())) {
                throw new SpecsAlreadyExistException(SPECS_ALREADY_EXIST);
            }
            ProductSku productSKU = ProductSku.builder()
                    .productId(product.getId())
                    .price(skuItem.getPrice())
                    .stock(skuItem.getStock())
                    .specs(skuItem.getSpecs())
                    .image(skuItem.getImage())
                    .status(ENABLED)
                    .build();
            productSkuList.add(productSKU);
        }
        productSkuMapper.insertBatch(productSkuList);
    }

    /**
     * 修改商品
     * @param productId 需要修改商品的id
     * @param merchantProductUpdateDTO 修改的数据
     */
    @Override
    @Transactional
    public void updateProduct(Long productId, MerchantProductUpdateDTO merchantProductUpdateDTO) {
        Product product = productMapper.selectById(productId);
        //如果商品不存在或已被逻辑删除，抛出异常
        if(product == null){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        //权限审查
        if(!product.getMerchantId().equals(StpUtil.getLoginIdAsLong())){
            throw new NoPermissionException(NO_PERMISSION);
        }
        //判断同一商家下商品是否同名
        String existedProductName = productMapper.selectExistedProductName(merchantProductUpdateDTO.getName(),StpUtil.getLoginIdAsLong());
        if(existedProductName != null){
            throw new ProductNameAlreadyExistException(PRODUCT_NAME_ALREADY_EXIST);
        }
        BeanUtils.copyProperties(merchantProductUpdateDTO,product);
        productMapper.updateById(product);

        //更新对应的SKU，使用对比更新的方式
        //1. 查询该商品现有的SKU
        List<ProductSku> existedSkuList = productSkuMapper.selectExistSkuList(product.getId());

        //2. 用一个list记录前端传过来的skuId
        List<Long> submittedIds = new ArrayList<>();

        //3.遍历前端传的sku列表
        for (ProductSkuItemDTO item : merchantProductUpdateDTO.getSkuList()) {
            if (item.getSkuId() != null) {
                // 有skuId的就不是前端新增的，即数据库里已有的，更新
                ProductSku sku = productSkuMapper.selectById(item.getSkuId());
                if (sku != null) {
                    sku.setSpecs(item.getSpecs());
                    sku.setPrice(item.getPrice());
                    sku.setStock(item.getStock());
                    sku.setImage(item.getImage());
                    productSkuMapper.updateById(sku);
                    submittedIds.add(item.getSkuId());
                }
            } else {
                // 无skuId则为新增的
                ProductSku sku = new ProductSku();
                sku.setProductId(productId);
                sku.setSpecs(item.getSpecs());
                sku.setPrice(item.getPrice());
                sku.setStock(item.getStock());
                sku.setImage(item.getImage());
                sku.setStatus(ENABLED);
                productSkuMapper.insert(sku);
            }
        }
        //4.数据库里有，但前端没传skuId的就意味着前端删除了，后端也删除（逻辑删除）
        for (ProductSku sku : existedSkuList) {
            if (!submittedIds.contains(sku.getId())) {
                productSkuMapper.deleteById(sku.getId());
            }
        }
    }

    /**
     * 修改商品状态
     * @param productId 需要修改商品的id
     */
    @Override
    public void changeStatus(Long productId, Integer status) {
        Product product = productMapper.selectById(productId);
        //如果商品不存在或已被逻辑删除，抛出异常
        if(product == null) {
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        //权限审查
        if(!product.getMerchantId().equals(StpUtil.getLoginIdAsLong())){
            throw new NoPermissionException(NO_PERMISSION);
        }
        product.setStatus(status);
        productMapper.updateById(product);
    }

    /**
     * 逻辑删除商品
     * @param productId 商品id
     */
    @Override
    public void logicDeleteProduct(Long productId) {
        Product product = productMapper.selectById(productId);
        if(product == null){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        //权限审查
        if(!product.getMerchantId().equals(StpUtil.getLoginIdAsLong())){
            throw new NoPermissionException(NO_PERMISSION);
        }
        productMapper.deleteById(productId);
    }

    /**
     * 复原逻辑删除的商品
     * @param productId 复原商品id
     */
    @Override
    public void recoveryProduct(Long productId) {
        Product product = productMapper.selectByIdIgnoreLogic(productId);
        if(product == null){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        //权限审查
        if(!product.getMerchantId().equals(StpUtil.getLoginIdAsLong())){
            throw new NoPermissionException(NO_PERMISSION);
        }
        //若本就未被删除就提示
        if(product.getDeleted() == DISABLED){
            throw new ProductHasNotDeletedException(PRODUCT_HAS_NOT_DELETED);
        }
        productMapper.recoveryProduct(productId);
    }

    /**
     * 彻底删除商品数据
     * @param productId 商品id
     */
    @Override
    public void deleteProduct(Long productId) {
        Product product = productMapper.selectByIdIgnoreLogic(productId);
        if(product == null){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        //权限审查
        if(!product.getMerchantId().equals(StpUtil.getLoginIdAsLong())){
            throw new NoPermissionException(NO_PERMISSION);
        }
        productMapper.realDelete(productId);
    }

    /**
     * 用户端分页查询商品分类
     * @param userProductPageQueryDTO 分页查询参数
     * @return 返回分页查询结果
     */
    @Override
    public PageResult<UserProductPageResultVO> userPageQuery(UserProductPageQueryDTO userProductPageQueryDTO) {
        //创建分页对象page
        Page<ProductPageResultBO> page = new Page<>(userProductPageQueryDTO.getPage(), userProductPageQueryDTO.getPageSize());
        //中转BO类介入，复制查询条件
        ProductPageQueryBO productPageQueryBO = new ProductPageQueryBO();
        BeanUtils.copyProperties(userProductPageQueryDTO,productPageQueryBO);
        productPageQueryBO.setStatus(ENABLED); //强制用户只能看到上架商品
        //分页查询
        Page<ProductPageResultBO> result = productMapper.pageQuery(page, productPageQueryBO);
        //总数
        long total = result.getTotal();
        //记录数
        List<UserProductPageResultVO> records = new ArrayList<>();
        //中转记录回VO
        for (ProductPageResultBO bo : result.getRecords()) {
            UserProductPageResultVO vo = UserProductPageResultVO.builder()
                    .id(bo.getId())
                    .merchantId(bo.getMerchantId())
                    .categoryId(bo.getCategoryId())
                    .brandId(bo.getBrandId())
                    .name(bo.getName())
                    .merchantName(bo.getMerchantName())
                    .categoryName(bo.getCategoryName())
                    .brandName(bo.getBrandName())
                    .mainImage(bo.getMainImage())
                    .build();
            records.add(vo);
        }
        //返回封装好的VO
        return new PageResult<>(total, records);
    }
    /**
     * 商家端分页查询商品分类
     * @param merchantProductPageQueryDTO 分页查询参数
     * @return 返回分页查询结果
     */
    @Override
    public PageResult<MerchantProductPageResultVO> merchantPageQuery(MerchantProductPageQueryDTO merchantProductPageQueryDTO) {
        Page<ProductPageResultBO> page = new Page<>(merchantProductPageQueryDTO.getPage(), merchantProductPageQueryDTO.getPageSize());
        ProductPageQueryBO productPageQueryBO = new ProductPageQueryBO();
        BeanUtils.copyProperties(merchantProductPageQueryDTO,productPageQueryBO);
        //给BO里加入当前商家id，便于让商家查询自家商品
        productPageQueryBO.setMerchantId(StpUtil.getLoginIdAsLong());
        Page<ProductPageResultBO> result = productMapper.pageQuery(page, productPageQueryBO);
        long total = result.getTotal();
        List<MerchantProductPageResultVO> records = new ArrayList<>();
        for (ProductPageResultBO bo : result.getRecords()) {
            MerchantProductPageResultVO vo = MerchantProductPageResultVO.builder()
                    .id(bo.getId())
                    .merchantId(bo.getMerchantId())
                    .categoryId(bo.getCategoryId())
                    .brandId(bo.getBrandId())
                    .name(bo.getName())
                    .merchantName(bo.getMerchantName())
                    .categoryName(bo.getCategoryName())
                    .brandName(bo.getBrandName())
                    .mainImage(bo.getMainImage())
                    .status(bo.getStatus())
                    .updateTime(bo.getUpdateTime())
                    .build();
            records.add(vo);
        }
        return new PageResult<>(total, records);
    }
}
