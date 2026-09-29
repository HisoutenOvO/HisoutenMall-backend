package cn.hisouten.mall.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.CartItemMapper;
import cn.hisouten.mall.pojo.bo.CartItemListBO;
import cn.hisouten.mall.pojo.dto.UserCartAddDTO;
import cn.hisouten.mall.pojo.dto.UserCartItemDeleteDTO;
import cn.hisouten.mall.pojo.entity.CartItem;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.entity.ProductSku;
import cn.hisouten.mall.pojo.vo.UserCartItemListVO;
import cn.hisouten.mall.service.CartService;
import cn.hisouten.mall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 查询购物车列表
     * @param userId 用户id
     * @return 返回值
     */
    @Override
    public List<UserCartItemListVO> listQuery(Long userId) {
        List<CartItemListBO> boList = cartItemMapper.selectListByUserId(userId);
        List<UserCartItemListVO> voList = new ArrayList<>();
        for (CartItemListBO bo : boList) {
            UserCartItemListVO vo = new UserCartItemListVO();
            //设置图片
            if (bo.getSkuImage() != null) {
                vo.setImage(bo.getSkuImage());
            } else {
                vo.setImage(bo.getMainImage());
            }
            // 失效判断
            if (bo.getSkuDeleted() == ENABLED || bo.getSkuStatus() != ENABLED) {
                vo.setInvalid(true);
                vo.setInvalidReason(SKU_HAS_DISABLED);
            } else if (bo.getProductDeleted() == ENABLED || bo.getProductStatus() != ENABLED) {
                vo.setInvalid(true);
                vo.setInvalidReason(PRODUCT_HAS_DISABLED);
            } else if (vo.getStock() < vo.getQuantity()) {
                vo.setInvalid(true);
                vo.setInvalidReason(OUT_OF_STOCK);
            } else {
                vo.setInvalid(false);
            }
            // 失效项强制取消勾选（只返回展示）
            if (vo.getInvalid()) {
                vo.setChecked(DISABLED);
            }
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 购物车修改数量
     * @param cartItemId 购物车单品id
     * @param quantity 最终数量
     */
    @Override
    public void updateQuantity(Long userId, Long cartItemId, Integer quantity) {
        CartItem item = cartItemMapper.selectById(cartItemId);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new BizException(CART_NOT_FOUND);
        }
        ProductSku sku = productService.selectSkuBySkuId(item.getSkuId());
        if (sku == null || quantity > sku.getStock()) {
            throw new BizException(OUT_OF_STOCK);
        }
        item.setQuantity(quantity);
        cartItemMapper.updateById(item);
    }

    /**
     * 用户单条勾选购物车记录
     * @param cartItemId 购物车项id
     * @param checked 勾选状态
     */
    @Override
    public void updateChecked(Long userId, Long cartItemId, Integer checked) {
        CartItem item = cartItemMapper.selectById(cartItemId);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new BizException(CART_NOT_FOUND);
        }
        // 如果要勾选，校验有效性
        ProductSku productSku = productService.selectSkuBySkuId(item.getSkuId());
        Product product = productService.selectProductByProductId(productSku.getProductId());
        if (checked == ENABLED && productSku.getStatus() == DISABLED && product.getStatus() == DISABLED) {
            throw new BizException("失效商品无法勾选");
        }
        item.setChecked(checked);
        cartItemMapper.updateById(item);
    }

    /**
     * 全选或全不选
     * @param checked 勾选状态
     */
    @Override
    public void checkAll(Integer checked) {
        Long userId = StpUtil.getLoginIdAsLong();
        if(checked == DISABLED){
            cartItemMapper.changeAllCheckedByUserId(userId,checked);
        }else if(checked == ENABLED){
            //选出有效的购物车id，先取消所有选择，再全选有效的
            List<Long> validIds = cartItemMapper.selectValidIdsByUserId(userId);
            cartItemMapper.changeAllCheckedByUserId(userId,checked);
            cartItemMapper.changeAllCheckedByCartItemId(validIds,ENABLED);
        }else{
            throw new BizException(SYSTEM_ERROR);
        }
    }

    /**
     * 用户批量删除购物车数据
     * @param userCartItemDeleteDTO 删除的id
     * @param userId 用户id
     */
    @Override
    public void deleteBatch(UserCartItemDeleteDTO userCartItemDeleteDTO,Long userId) {
        List<Long> ids = userCartItemDeleteDTO.getIds();
        if (ids == null || ids.isEmpty()) return;
        cartItemMapper.deleteBatchIds(ids);
    }


}
