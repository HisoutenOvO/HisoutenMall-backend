package cn.hisouten.mall.pojo.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateDTO {

    private Long categoryId;

    private Long brandId;

    private String name;

    private String subtitle;

    private String mainImage;

    private String detail;

    private Integer status;

    private Integer deleted;

}
