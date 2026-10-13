package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.mapper.HistoryMapper;
import cn.hisouten.mall.mapper.ProductMapper;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.entity.UserBrowseHistory;
import cn.hisouten.mall.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryMapper historyMapper;
    private final ProductMapper productMapper;

    /**
     * 用户新增浏览记录
     * @param userId 用户id
     * @param productId 商品id
     */
    @Override
    public void addHistory(Long userId, Long productId) {
        Product product = productMapper.selectById(productId);
        if(product == null){
            return;
        }
        historyMapper.upsert(userId,productId);
    }
}
