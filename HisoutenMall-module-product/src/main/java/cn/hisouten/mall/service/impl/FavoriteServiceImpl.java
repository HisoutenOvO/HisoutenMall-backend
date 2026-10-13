package cn.hisouten.mall.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.FavoriteMapper;
import cn.hisouten.mall.mapper.ProductMapper;
import cn.hisouten.mall.pojo.BasePageQueryDTO;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.entity.Favorite;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.vo.product.UserFavoritePageResultVO;
import cn.hisouten.mall.service.FavoriteService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.PRODUCT_NOT_FOUND;
import static cn.hisouten.mall.constant.ExceptionMessageConstant.SYSTEM_ERROR;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteMapper favoriteMapper;
    private final ProductMapper productMapper;

    /**
     * 用户收藏商品
     * @param userId 用户id
     * @param productId 商品id
     */
    @Override
    public void addFavorite(Long userId, Long productId) {
        Product product = productMapper.selectById(productId);
        if(product == null){
            throw new BizException(PRODUCT_NOT_FOUND);
        }
        Product existedProduct = favoriteMapper.selectProductById(userId,productId);
        if(existedProduct != null){
            throw new BizException(SYSTEM_ERROR);
        }
        Favorite favorite = new Favorite();
        favorite.setProductId(productId);
        favorite.setUserId(userId);
        favoriteMapper.insert(favorite);
    }

    /**
     * 用户取消收藏商品
     * @param userId 用户id
     * @param productId 商品id
     */
    @Override
    public void cancelFavorite(Long userId, Long productId) {
        Product product = productMapper.selectById(productId);
        if(product == null){
            return;
        }
        favoriteMapper.deleteById(productId);
    }

    /**
     * 用户分页查询收藏
     * @return 返回值
     */
    @Override
    public PageResult<UserFavoritePageResultVO> pageQuery(BasePageQueryDTO pageQueryDTO) {
        Page<UserFavoritePageResultVO> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        Page<UserFavoritePageResultVO> result = favoriteMapper.pageQuery(page, StpUtil.getLoginIdAsLong());
        long total = result.getTotal();
        return new PageResult<>(total,result.getRecords());
    }
}
