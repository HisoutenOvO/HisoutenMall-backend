package cn.hisouten.mall.pojo.dto.order;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户分页查询订单参数
 */
@Data
public class UserOrderPageQueryDTO extends BasePageQueryDTO {

    @Size(max = 64, message = "搜索关键词过长")
    private String keyword;

    @Size(max = 50, message = "商家名称过长")
    private String merchantName;

    @Size(max = 100, message = "商品名称过长")
    private String productName;

    private Integer status;

    private Integer source;
}