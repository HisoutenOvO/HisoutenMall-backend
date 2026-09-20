package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.businessexception.ProductHasNotDeletedException;
import cn.hisouten.mall.exception.businessexception.ProductNotFoundException;
import cn.hisouten.mall.mapper.ProductMapper;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.bo.product.ProductPageQueryBO;
import cn.hisouten.mall.pojo.dto.product.MerchantProductAddDTO;
import cn.hisouten.mall.pojo.dto.product.MerchantProductPageQueryDTO;
import cn.hisouten.mall.pojo.dto.product.MerchantProductUpdateDTO;
import cn.hisouten.mall.pojo.dto.product.UserProductPageQueryDTO;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.bo.product.ProductPageResultBO;
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

import java.util.ArrayList;
import java.util.List;

import static cn.hisouten.mall.exception.constant.ExceptionMessageConstant.PRODUCT_HAS_NOT_DELETED;
import static cn.hisouten.mall.exception.constant.ExceptionMessageConstant.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    //注入自己的mapper
    private final ProductMapper productMapper;
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
    public void addProduct(MerchantProductAddDTO merchantProductAddDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(merchantProductAddDTO,product);
        productMapper.insert(product);
    }

    /**
     * 修改商品
     * @param productId 需要修改商品的id
     * @param merchantProductUpdateDTO 修改的数据
     */
    @Override
    public void updateProduct(Long productId, MerchantProductUpdateDTO merchantProductUpdateDTO) {
        Product product = productMapper.selectById(productId);
        //如果商品不存在或已被逻辑删除，抛出异常
        if(product == null){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        BeanUtils.copyProperties(merchantProductUpdateDTO,product);
        productMapper.updateById(product);
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
        productMapper.deleteById(productId);
    }

    /**
     * f复原逻辑删除的商品
     * @param productId 复原商品id
     */
    @Override
    public void recoveryProduct(Long productId) {
        Product product = productMapper.selectByIdIgnoreLogic(productId);
        if(product == null){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        //若本就未被删除就提示
        if(product.getDeleted() == 0){
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
     * 用户端分页查询商品分类
     * @param merchantProductPageQueryDTO 分页查询参数
     * @return 返回分页查询结果
     */
    @Override
    public PageResult<MerchantProductPageResultVO> merchantPageQuery(MerchantProductPageQueryDTO merchantProductPageQueryDTO) {
        Page<ProductPageResultBO> page = new Page<>(merchantProductPageQueryDTO.getPage(), merchantProductPageQueryDTO.getPageSize());
        ProductPageQueryBO productPageQueryBO = new ProductPageQueryBO();
        BeanUtils.copyProperties(merchantProductPageQueryDTO,productPageQueryBO);
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
                    .deleted(bo.getDeleted())
                    .build();
            records.add(vo);
        }
        return new PageResult<>(total, records);
    }
}
