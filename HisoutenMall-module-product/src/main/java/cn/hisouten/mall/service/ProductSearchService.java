package cn.hisouten.mall.service;

import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.product.UserProductPageQueryDTO;
import cn.hisouten.mall.pojo.vo.product.UserProductPageResultVO;
import jakarta.validation.Valid;

public interface ProductSearchService {
    /**
     * 用户分页查询商品
     * @param userProductPageQueryDTO 查询参数
     * @return 返回值
     */
    PageResult<UserProductPageResultVO> userPageQuery(@Valid UserProductPageQueryDTO userProductPageQueryDTO);

    /**
     * 根据商品id删除文档
     * @param productId 商品id
     */
    void deleteById(Long productId);

    /**
     * 根据商品id全量更新文档
     * @param productId 商品id
     */
    void syncToEs(Long productId);

    /**
     *
     * @param productId
     * @param totalSales
     */
    void updateSalesInEs(Long productId, Integer totalSales);
}
