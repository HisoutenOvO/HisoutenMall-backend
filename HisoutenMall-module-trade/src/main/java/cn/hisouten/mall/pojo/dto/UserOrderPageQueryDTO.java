package cn.hisouten.mall.pojo.dto;

import cn.hisouten.mall.pojo.BasePageQuery;
import lombok.Data;

/**
 * 用户分页查询订单参数
 */
@Data
public class UserOrderPageQueryDTO extends BasePageQuery {
    private String keyword;

    private Integer status;
}
