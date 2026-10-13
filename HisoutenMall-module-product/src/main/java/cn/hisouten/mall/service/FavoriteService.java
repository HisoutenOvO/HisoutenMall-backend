package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.BasePageQueryDTO;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.vo.product.UserFavoritePageResultVO;

public interface FavoriteService {

    /**
     * 用户收藏商品
     * @param userId 用户id
     * @param productId 商品id
     */
    void addFavorite(Long userId, Long productId);

    /**
     * 用户取消收藏商品
     * @param userId 用户id
     * @param productId 商品id
     */
    void cancelFavorite(Long userId, Long productId);

    /**
     * 用户分页查询收藏
     * @return 返回值
     */
    PageResult<UserFavoritePageResultVO> pageQuery(BasePageQueryDTO pageQueryDTO);

    /**
     * 判断用户是否收藏商品
     * @param userId 用户id
     * @param productId 商品id
     * @return 返回值
     */
    Boolean checkFavorite(Long userId, Long productId);
}
