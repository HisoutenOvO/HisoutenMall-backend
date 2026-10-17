package cn.hisouten.mall.pojo.dto.order;

import cn.hisouten.mall.pojo.BasePageQueryDTO;
import lombok.Data;

/**
 * 管理员分页查询订单参数
 */
@Data
public class AdminOrderPageQueryDTO extends BasePageQueryDTO {
    private String keyword;

    private Integer status;

    private Integer source;
}
