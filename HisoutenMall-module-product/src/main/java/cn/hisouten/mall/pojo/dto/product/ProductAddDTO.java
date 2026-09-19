package cn.hisouten.mall.pojo.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductAddDTO {

    private String name;

    private Long merchantId;

    private String subtitle;

    private Long categoryId;

    private Long brandId;

    private String mainImage;

    private Integer status;

    private String detail;

    private Integer deleted;

}
