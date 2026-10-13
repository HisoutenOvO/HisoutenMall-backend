package cn.hisouten.mall.service;

public interface FavoriteService {

    /**
     * 用户收藏商品
     * @param userId 用户id
     * @param productId 商品id
     */
    void addFavorite(Long userId, Long productId);
}
