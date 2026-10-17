package cn.hisouten.mall.pojo.dto.order;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 商家端分页查询订单参数
 */
@Data
public class MerchantOrderPageQueryDTO extends BasePageQueryDTO {

    @Size(max = 64, message = "搜索关键词过长")
    private String keyword;

    private Integer status;

    private Integer source;
}