package cn.hisouten.mall.pojo.dto.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MerchantSeckillProductAddDTO {

    @NotNull(message = "活动ID不能为空")
    private Long activityId;

    @NotEmpty(message = "秒杀商品列表不能为空")
    @Valid
    private List<SeckillProductItem> items;   // 支持批量

    @Data
    public static class SeckillProductItem {

        @NotNull(message = "SKU ID不能为空")
        private Long skuId;

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

}