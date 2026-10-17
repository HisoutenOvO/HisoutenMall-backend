package cn.hisouten.mall.pojo.dto.cart;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 用户批量删除购物车参数
 */
@Data
public class UserCartItemDeleteDTO {

    @NotEmpty(message = "要删除的购物车项ID列表不能为空")
    private List<Long> ids;
}