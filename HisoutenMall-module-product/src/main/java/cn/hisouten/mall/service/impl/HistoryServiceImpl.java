package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.mapper.HistoryMapper;
import cn.hisouten.mall.mapper.ProductMapper;
import cn.hisouten.mall.pojo.BasePageQueryDTO;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.entity.UserBrowseHistory;
import cn.hisouten.mall.pojo.vo.history.UserHistoryPageResultVO;
import cn.hisouten.mall.service.HistoryService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 用户分页查询历史记录
     * @param userId 用户id
     * @param pageQueryDTO 分页参数
     * @return 返回值
     */
    @Override
    public PageResult<UserHistoryPageResultVO> pageQuery(Long userId, BasePageQueryDTO pageQueryDTO) {
        Page<UserHistoryPageResultVO> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        Page<UserHistoryPageResultVO> result = historyMapper.pageQuery(userId,page);
        return new PageResult<>(result.getTotal(), result.getRecords());
    }
}
