package cn.hisouten.mall.pojo.dto.order;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 用户支付订单参数
 */
@Data
public class UserOrderPayDTO {

    @NotEmpty(message = "订单号列表不能为空")
    List<String> orderNos;
}