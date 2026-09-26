package cn.hisouten.mall.pojo.bo.product;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用于product的分页查询中转VO
 */
@Data
public class ProductPageResultBO {

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