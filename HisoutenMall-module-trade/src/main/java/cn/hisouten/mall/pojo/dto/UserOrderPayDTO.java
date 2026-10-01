package cn.hisouten.mall.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户支付订单参数
 */
@Data
public class UserOrderPayDTO {
    List<String> orderNos; //是订单号，不是订单id，因为刚才给vo传过去的就是no
}
