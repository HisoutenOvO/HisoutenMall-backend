package cn.hisouten.mall.pojo.dto.product;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import lombok.Data;

/**
 * 管理端分页查询商品参数
 */
@Data
public class AdminProductPageQueryDTO extends BasePageQueryDTO {
    private String keyword;

    private Long categoryId;

    private Long brandId;

    private Integer status;

    private Long merchantId;

    private Integer deleted;

    private String merchantName;
}
