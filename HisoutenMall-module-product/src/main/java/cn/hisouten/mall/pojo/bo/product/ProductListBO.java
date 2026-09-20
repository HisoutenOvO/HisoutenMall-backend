package cn.hisouten.mall.pojo.bo.product;

import lombok.Data;

/**
 * 用于层间中转信息
 */
@Data
public class ProductListBO {

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
}