package cn.hisouten.mall.pojo.vo.product;


import lombok.Data;

import java.util.List;

/**
 * 用户查询商品详情返回值
 */
@Data
public class UserProductDetailVO {

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

    private List<UserProductSkuVO> skuList;

}