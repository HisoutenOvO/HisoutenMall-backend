package cn.hisouten.mall.pojo.vo.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProductListVO {

    private Long id;

    private Long merchantId;

    private Long categoryId;

    private Long brandId;

    private String name;

    private String merchantName;

    private String categoryName;

    private String brandName;

    private String mainImage;
}
