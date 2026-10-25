package cn.hisouten.mall.pojo.vo.order;

import cn.hisouten.mall.pojo.bo.cart.CartItemListBO;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 用户预计算订单信息返回值
 */
@Data
public class UserOrderPreviewVO {

    private BigDecimal totalProductAmount; // 商品总金额

    private BigDecimal totalDiscountAmount;// 总优惠金额

    private BigDecimal payAmount;          // 最终需支付金额

    private List<MerchantPreview> merchantList;

    @Data
    public static class MerchantPreview {

        private Long merchantId;

        private String merchantName;

        private BigDecimal merchantProductAmount; // 本店商品总价

        private BigDecimal merchantDiscount;      // 本店优惠金额

        private BigDecimal merchantPayAmount;     // 本店实付金额

        private List<CartItemListBO> items;       // 本店商品明细
    }
}