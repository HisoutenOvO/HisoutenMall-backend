package cn.hisouten.mall.pojo.vo.product;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商家查询商品详情返回值
 */
@Data
public class MerchantProductDetailVO {

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

    private List<MerchantProductSkuVO> skuList;

}
