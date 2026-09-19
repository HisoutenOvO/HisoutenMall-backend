package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.mapper.ProductMapper;
import cn.hisouten.mall.pojo.dto.product.ProductAddDTO;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.vo.product.ProductDetailVO;
import cn.hisouten.mall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;

    /**
     * 用户端查询商品详情
     * @param productId 商品ID
     * @return 返回商品详情信息
     */
    @Override
    public ProductDetailVO detailQuery(Long productId) {
        Product product = productMapper.selectById(productId);
        ProductDetailVO productDetailVO = new ProductDetailVO();
        BeanUtils.copyProperties(product,productDetailVO);
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
}
