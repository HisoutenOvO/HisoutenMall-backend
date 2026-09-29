package cn.hisouten.mall.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用户查询购物车列表返回值
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCartItemListVO {
    private Long id;              // cart_item.id

    private Long productId;

    private Long skuId;

    private String productName;

    private String skuSpecs;

    private String image;

    private BigDecimal price;

    private Integer quantity;

    private Integer stock;

    private Integer checked;

    private Boolean invalid;      // 是否失效

    private String invalidReason;
}
