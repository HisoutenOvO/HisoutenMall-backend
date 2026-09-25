package cn.hisouten.mall.pojo.vo.product;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户所见商品SKU返回值
 */
@Data
public class UserProductSkuVO {

    private Long id;

    private String specs;

    private BigDecimal price;

    private Integer stock;

    private String image;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
