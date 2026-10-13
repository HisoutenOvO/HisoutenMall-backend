package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.BasePageQueryDTO;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.vo.history.UserHistoryPageResultVO;

public interface HistoryService {

    /**
     * 用户新增浏览记录
     * @param userId 用户id
     * @param productId 商品id
     */
    void addHistory(Long userId, Long productId);

    /**
     * 用户分页查询历史记录
     * @param userId 用户id
     * @param pageQueryDTO 分页参数
     * @return 返回值
     */
    PageResult<UserHistoryPageResultVO> pageQuery(Long userId, BasePageQueryDTO pageQueryDTO);
}
