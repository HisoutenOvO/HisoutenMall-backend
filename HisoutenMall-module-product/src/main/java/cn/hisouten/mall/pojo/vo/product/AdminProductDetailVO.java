package cn.hisouten.mall.pojo.vo.product;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 管理员查询商品详情
 */
@Data
@NoArgsConstructor
public class AdminProductDetailVO {
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

    private Integer status;

    private Integer deleted;

    private List<AdminProductSkuVO> skuList;
}
