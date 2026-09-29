package cn.hisouten.mall.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用于中转购物车列表数据的BO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemListBO {
    private Long id;

    private Long productId;

    private Long skuId;

    private String productName;

    private String skuSpecs;

    private String skuImage;         // 优先 SKU 图片，为空用商品主图

    private String mainImage;

    private BigDecimal price;

    private Integer quantity;

    private Integer stock;

    private Integer checked;

    private Boolean invalid;

    private String invalidReason;

    private Integer skuStatus;

    private Integer productStatus;

    private Integer skuDeleted;

    private Integer productDeleted;
}
