package cn.hisouten.mall.pojo.dto;

import cn.hisouten.mall.pojo.BasePageQueryDTO;
import lombok.Data;

/**
 * 用户分页查询订单参数
 */
@Data
public class UserOrderPageQueryDTO extends BasePageQueryDTO {
    private String keyword;

    private Integer status;

    private Integer source;
}
