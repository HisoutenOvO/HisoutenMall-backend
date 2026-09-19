package cn.hisouten.mall.pojo.vo.product;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductDetailVO {

    private Long id;

    private String name;

    private String subtitle;

    private Long categoryId;

    private Long merchantId;

    private String merchantName;

    private Long brandId;

    private String categoryName;

    private String brandName;

    private String mainImage;

    private String detail;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer status;

    private Integer deleted;

}
