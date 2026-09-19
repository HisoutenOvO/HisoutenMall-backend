package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.businessexception.ProductHasNotDeletedException;
import cn.hisouten.mall.exception.businessexception.ProductNotFoundException;
import cn.hisouten.mall.mapper.ProductMapper;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.category.CategoryPageQueryDTO;
import cn.hisouten.mall.pojo.dto.product.ProductAddDTO;
import cn.hisouten.mall.pojo.dto.product.ProductUpdateDTO;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.vo.category.CategoryListVO;
import cn.hisouten.mall.pojo.vo.product.ProductDetailVO;
import cn.hisouten.mall.service.BrandService;
import cn.hisouten.mall.service.CategoryService;
import cn.hisouten.mall.service.ProductService;
import cn.hisouten.mall.user.service.MerchantProfileService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
     * 双端查询商品详情
     * @param productId 商品ID
     * @return 返回商品详情信息
     */
    @Override
    public ProductDetailVO detailQuery(Long productId) {
        Product product = productMapper.selectById(productId);
        //如果商品不存在或已被逻辑删除，抛出异常
        if(product == null){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        ProductDetailVO productDetailVO = new ProductDetailVO();
        BeanUtils.copyProperties(product,productDetailVO);
        String categoryName = categoryService.getCategoryNameByCategoryId(product.getCategoryId());
        String merchantName = merchantProfileService.getMerchantNameByMerchantId(product.getMerchantId());
        String brandName = brandService.getBrandNameByBrandId(product.getBrandId());
        productDetailVO.setBrandName(brandName);
        productDetailVO.setCategoryName(categoryName);
        productDetailVO.setMerchantName(merchantName);

        return productDetailVO;
    }

    /**
     * 商家端新增商品
     * @param productAddDTO 商品内容
     */
    @Override
    public void addProduct(ProductAddDTO productAddDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productAddDTO,product);
        productMapper.insert(product);
    }

    /**
     * 修改商品
     * @param productId 需要修改商品的id
     * @param productUpdateDTO 修改的数据
     */
    @Override
    public void updateProduct(Long productId, ProductUpdateDTO productUpdateDTO) {
        Product product = productMapper.selectById(productId);
        //如果商品不存在或已被逻辑删除，抛出异常
        if(product == null){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        BeanUtils.copyProperties(productUpdateDTO,product);
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
     * 分页查询商品分类
     * @param categoryPageQueryDTO 分页查询参数
     * @return 返回分页查询结果
     */
    @Override
    public PageResult<CategoryListVO> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        Page<Product> page = new Page<>(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        String keyword = categoryPageQueryDTO.getKeyword();
        Page<CategoryListVO> result = productMapper.pageQuery(page, keyword);
        long total = result.getTotal();
        List<CategoryListVO> records = result.getRecords();
        return new PageResult<>(total, records);
    }
}
