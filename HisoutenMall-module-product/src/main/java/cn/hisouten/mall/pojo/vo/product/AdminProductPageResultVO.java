package cn.hisouten.mall.pojo.vo.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理端分页查询商品返回值
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProductPageResultVO {
    private Long id;

    private Long merchantId;

    private Long categoryId;

    private Long brandId;

    private String name;

    private Integer status;

    private String merchantName;

    private String categoryName;

    private String brandName;

    private Integer deleted;

    private String mainImage;

    private BigDecimal minPrice;

    private Integer totalStock;

    private LocalDateTime updateTime;
}
