package cn.hisouten.mall.pojo.vo.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailVO {

    private Long id;

    private String name;

    private String subtitle;

    private String categoryName;

    private String merchantName;

    private String brandName;

    private String mainImage;

    private String detail;
}
