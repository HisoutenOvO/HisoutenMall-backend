package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.BizException;
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

import static cn.hisouten.mall.constant.CommonConstant.DEFAULT_HISTORY_LIMIT;
import static cn.hisouten.mall.constant.ExceptionMessageConstant.NO_PERMISSION;

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
        //若超出100条则删除最早的记录
        historyMapper.deleteExcess(userId, DEFAULT_HISTORY_LIMIT);
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

    /**
     * 用户删除单条历史记录
     * @param userId 用户id
     * @param productId 商品id
     */
    @Override
    public void deleteHistory(Long userId, Long productId) {
        UserBrowseHistory history = historyMapper.selectByProductId(userId,productId);
        if(!history.getUserId().equals(userId)){
            throw new BizException(NO_PERMISSION);
        }
        historyMapper.deleteById(history);
    }
}
