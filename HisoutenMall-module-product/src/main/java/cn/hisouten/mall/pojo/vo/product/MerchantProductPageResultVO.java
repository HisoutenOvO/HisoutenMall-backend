package cn.hisouten.mall.pojo.vo.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 商家分页查询返回值
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantProductPageResultVO {

    private Long id;

    private Long merchantId;

    private Long categoryId;

    private Long brandId;

    private String name;

    private Integer status;

    private String merchantName;

    private String categoryName;

    private String brandName;

    private String mainImage;

    private LocalDateTime updateTime;
}
