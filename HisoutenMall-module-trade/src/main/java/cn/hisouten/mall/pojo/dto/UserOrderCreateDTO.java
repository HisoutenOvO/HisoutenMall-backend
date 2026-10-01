package cn.hisouten.mall.pojo.dto;

import lombok.Data;

/**
 * 用户创建订单参数
 */
@Data
public class UserOrderCreateDTO {
    private Long addressId;

    private Long skuId;

    private Integer quantity;
}
