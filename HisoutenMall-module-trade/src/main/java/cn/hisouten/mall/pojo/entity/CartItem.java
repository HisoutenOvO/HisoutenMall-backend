package cn.hisouten.mall.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {
    private Long id;

    private Long userId;

    private Long productId;

    private Long skuId;

    private Integer quantity;

    private Integer checked;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
