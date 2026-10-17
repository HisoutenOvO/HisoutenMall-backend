package cn.hisouten.mall.pojo.dto.product;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MerchantSeckillProductAddDTO {

    private Long activityId;

    private List<SeckillProductItem> items;   // 支持批量

    @Data
    public static class SeckillProductItem {
        private Long skuId;
        private BigDecimal seckillPrice;
        private Integer seckillStock;
        private Integer limitPerUser;
    }

}
