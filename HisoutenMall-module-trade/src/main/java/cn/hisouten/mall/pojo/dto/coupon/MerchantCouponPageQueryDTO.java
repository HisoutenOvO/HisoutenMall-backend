package cn.hisouten.mall.pojo.dto.coupon;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 商家端分页查询优惠券参数
 */
@Data
public class MerchantCouponPageQueryDTO extends BasePageQueryDTO {

    @Size(max = 50, message = "搜索关键词过长")
    private String keyword;

    private Integer status;

    private Integer type;
}