package cn.hisouten.mall.pojo.dto.product;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品SKU主要项参数
 */
@Data
@NoArgsConstructor
public class ProductSkuItemDTO {

    private Long skuId;

    private String specs;

    private BigDecimal price;

    private Integer stock;

    private String image;
}
