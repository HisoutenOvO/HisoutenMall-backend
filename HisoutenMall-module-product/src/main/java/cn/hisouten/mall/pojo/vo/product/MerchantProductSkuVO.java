package cn.hisouten.mall.pojo.vo.product;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商家所见商品SKU返回值
 */
@Data
public class MerchantProductSkuVO {

    private Long id;

    private String specs;

    private BigDecimal price;

    private Integer stock;

    private String image;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;

}
