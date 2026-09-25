package cn.hisouten.mall.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品SKU实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSku {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long productId;

    private Long skuCode;

    private BigDecimal price;

    private Integer stock;

    private String specs;

    private String image;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
