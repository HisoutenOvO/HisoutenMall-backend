package cn.hisouten.mall.pojo.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商家端修改秒杀商品参数
 */
@Data
public class MerchantSeckillProductUpdateDTO {

    @NotNull(message = "秒杀价格不能为空")
    @DecimalMin(value = "0.01", message = "秒杀价格必须大于0")
    private BigDecimal seckillPrice;

    @NotNull(message = "秒杀库存不能为空")
    @Min(value = 1, message = "秒杀库存最少为1")
    private Integer seckillStock;

    @NotNull(message = "限购数量不能为空")
    @Min(value = 1, message = "每人限购最少为1")
    private Integer limitPerUser;

}