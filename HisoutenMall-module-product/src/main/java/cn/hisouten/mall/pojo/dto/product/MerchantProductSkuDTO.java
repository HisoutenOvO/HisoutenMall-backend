package cn.hisouten.mall.pojo.dto.product;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商家修改sku参数
 */
@Data
public class MerchantProductSkuDTO {

    private String specs;

    private BigDecimal price;

    private Integer stock;

    private String image;

}
