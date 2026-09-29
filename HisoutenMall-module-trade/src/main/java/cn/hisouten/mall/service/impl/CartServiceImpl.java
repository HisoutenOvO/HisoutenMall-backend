package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.CartItemMapper;
import cn.hisouten.mall.pojo.dto.UserCartAddDTO;
import cn.hisouten.mall.pojo.entity.CartItem;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.entity.ProductSku;
import cn.hisouten.mall.service.CartService;
import cn.hisouten.mall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.StatusConstant.DISABLED;
import static cn.hisouten.mall.constant.StatusConstant.ENABLED;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    //定义购物车条目上限
    public static final int CART_UPPER_LIMIT = 100;

    private final CartItemMapper cartItemMapper;
    private final ProductService productService;

    /**
     * 用户新增购物车
     * @param userCartAddDTO 新增参数
     */
    @Override
    public void addCart(Long userId, UserCartAddDTO userCartAddDTO) {
        //校验商品及商品规格是否存在且合法
        ProductSku productSku = productService.selectSkuBySkuId(userCartAddDTO.getSkuId());
        if(productSku == null){
            throw new BizException(SKU_NOT_FOUND);
        }
        if(productSku.getStatus() == DISABLED){
            throw new BizException(SKU_HAS_DISABLED);
        }
        Product product = productService.selectProductByProductId(productSku.getProductId());
        if(product == null){
            throw new BizException(PRODUCT_NOT_FOUND);
        }
        if(product.getStatus() == DISABLED){
            throw new BizException(PRODUCT_HAS_DISABLED);
        }
        //查询购物车是否有商品，没有则增加有则数量加一，以及购物车商品条目数量和库存数量检查
        CartItem cartItem = cartItemMapper.selectByUserIdAndSkuId(userId,userCartAddDTO.getSkuId());
        if(cartItem != null){
            //检查库存容量
            int newQty = cartItem.getQuantity() + userCartAddDTO.getQuantity();
            if(newQty > productSku.getStock()){
                throw new BizException(OUT_OF_STOCK);
            }
            cartItem.setQuantity(newQty);
            cartItemMapper.updateById(cartItem);
        }else{
            //购物车上限检查
            int cartCount = cartItemMapper.selectCartCount(userId);
            if(cartCount > CART_UPPER_LIMIT){
                throw new BizException(OUT_OF_CART);
            }
            if(userCartAddDTO.getQuantity() > productSku.getStock()){
                throw new BizException(OUT_OF_STOCK);
            }
            CartItem item = new CartItem();
            item.setUserId(userId);
            item.setProductId(productSku.getProductId());
            item.setSkuId(userCartAddDTO.getSkuId());
            item.setQuantity(userCartAddDTO.getQuantity());
            item.setChecked(ENABLED);
            cartItemMapper.insert(item);
        }
    }
}
