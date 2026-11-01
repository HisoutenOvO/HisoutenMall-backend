package cn.hisouten.mall.mq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;

/**
 * 商品增加销量消息体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSalesMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long productId;

    //增加销量
    private Integer quantity;
}