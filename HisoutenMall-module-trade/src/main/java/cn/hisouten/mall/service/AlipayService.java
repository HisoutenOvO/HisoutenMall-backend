package cn.hisouten.mall.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AlipayService {
    /**
     * 生成支付表单
     * @param orderNo 订单号
     * @return 返回值
     */
    String createPayForm(String orderNo);

    /**
     * 处理支付宝回调
     * @param params 支付宝回调参数
     */
    void handleNotify(Map<String, String> params);
}