package cn.hisouten.mall.pojo.dto.order;

import lombok.Data;
import java.util.List;

/**
 * 用户预计算下单信息参数
 */
@Data
public class UserOrderPreviewDTO {
    private Long addressId;
    private List<Long> userCouponIds;
}