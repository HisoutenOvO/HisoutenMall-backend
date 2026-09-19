package cn.hisouten.mall.pojo.vo.product;

import lombok.Data;

@Data
public class ProductListVO {

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
