package cn.hisouten.mall.pojo.vo.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailVO {

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Integer status;

    private Integer deleted;

}
