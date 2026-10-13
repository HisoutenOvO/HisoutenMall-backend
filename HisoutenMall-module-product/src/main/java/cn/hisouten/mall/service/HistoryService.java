package cn.hisouten.mall.service;

public interface HistoryService {

    /**
     * 用户新增浏览记录
     * @param userId 用户id
     * @param productId 商品id
     */
    void addHistory(Long userId, Long productId);
}
