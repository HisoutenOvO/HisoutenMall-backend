package cn.hisouten.mall.pojo.dto.order;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理员分页查询订单参数
 */
@Data
public class AdminOrderPageQueryDTO extends BasePageQueryDTO {

    @Size(max = 64, message = "订单过长")
    private String orderNo;

    @Size(max = 64, message = "商家名称过长")
    private String merchantName;

    @Size(max = 64, message = "买家名称过长")
    private String buyerName;

    private Integer status;

    private Integer source;
}