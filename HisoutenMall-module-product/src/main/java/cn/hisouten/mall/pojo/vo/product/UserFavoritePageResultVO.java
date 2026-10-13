package cn.hisouten.mall.pojo.vo.product;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用户分页查询收藏返回值
 */
@Data
@NoArgsConstructor
public class UserFavoritePageResultVO {

    private Long id;              // favorite.id

    private Long productId;

    private String productName;

    private String mainImage;

    private BigDecimal minPrice;   // 最低价（从 SKU 取）

    private Integer totalStock;    // 总库存

    private Integer productStatus; // 商品状态（下架了的显示"已下架"）

    private String merchantName;

}
