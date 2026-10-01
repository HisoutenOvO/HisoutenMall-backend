package cn.hisouten.mall.pojo.dto;

import cn.hisouten.mall.pojo.BasePageQuery;
import lombok.Data;

/**
 * 管理员分页查询订单参数
 */
@Data
public class AdminOrderPageQueryDTO extends BasePageQuery {
    private String keyword;

    private Integer status;
}
