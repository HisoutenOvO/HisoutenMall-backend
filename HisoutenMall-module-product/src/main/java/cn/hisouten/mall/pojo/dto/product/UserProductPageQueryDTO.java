package cn.hisouten.mall.pojo.dto.product;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户分页查询商品参数
 */
@Data
public class UserProductPageQueryDTO extends BasePageQueryDTO {

    @Size(max = 100, message = "搜索关键词过长")
    private String keyword;

    private Long categoryId;

    private Long merchantId;

    private Long brandId;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    /**
     * 排序方式:
     * 0-综合(默认算分)
     * 1-销量
     * 2-价格升
     * 3-价格降
     * 4-新品
     */
    private Integer sortType;

}