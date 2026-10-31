package cn.hisouten.mall.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.dao.mapper.FavoriteMapper;
import cn.hisouten.mall.dao.mapper.ProductMapper;
import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.pojo.entity.UserFavorite;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.vo.favorite.UserFavoritePageResultVO;
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
        UserFavorite userFavorite = new UserFavorite();
        userFavorite.setProductId(productId);
        userFavorite.setUserId(userId);
        favoriteMapper.insert(userFavorite);
    }

    /**
     * 用户取消收藏商品
     * @param userId 用户id
     * @param productId 商品id
     */
    @Override
    public void cancelFavorite(Long userId, Long productId) {
        favoriteMapper.deleteByProductId(userId,productId);
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

    /**
     * 判断用户是否收藏商品
     * @param userId 用户id
     * @param productId 商品id
     * @return 返回值
     */
    @Override
    public Boolean checkFavorite(Long userId, Long productId) {
        Integer count = favoriteMapper.selectCount(userId,productId);
        return count != null && count > 0;
    }

    /**
     * 获取收藏数量
     * @param userId 用户if
     * @return 返回值
     */
    @Override
    public Integer count(Long userId) {
        return Math.toIntExact(favoriteMapper.selectCount(userId,null));
    }

    /**
     * 根据商品id删除收藏信息
     * @param productId 商品id
     */
    @Override
    public void deleteByProductId(Long productId) {
        favoriteMapper.deleteByProductId(null,productId);
    }
}
