package cn.hisouten.mall.pojo.dto;

import lombok.Data;

/**
 * 用户购物车新增参数
 */
@Data
public class UserCartAddDTO {
    private Long skuId;

    private Integer quantity;
}
