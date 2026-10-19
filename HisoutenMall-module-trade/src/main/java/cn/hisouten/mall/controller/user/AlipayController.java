package cn.hisouten.mall.controller.user;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hisouten.mall.common.annotation.RepeatSubmit;
import cn.hisouten.mall.service.AlipayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "用户端——支付接口")
public class AlipayController {

    private final AlipayService alipayService;

    /**
     * 支付宝支付接口
     * @param orderNo 订单号
     * @param response HttpServletResponse
     * @throws IOException 抛出异常
     */
    @SaIgnore
    @GetMapping("/alipay")
    @RepeatSubmit
    @Operation(summary = "支付宝支付")
    public void pay(@RequestParam String orderNo, HttpServletResponse response) throws IOException {
        String form = alipayService.createPayForm(orderNo);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(form);
        response.getWriter().flush();
    }

    /**
     * 支付宝支付回调接口
     * @param request HttpServletRequest
     * @return 返回值
     */
    @SaIgnore
    @PostMapping("/notify")
    @Operation(summary = "支付宝支付回调")
    public String notify(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> params.put(k, v[0]));
        try {
            alipayService.handleNotify(params);
            return "success"; // 业务处理成功，告诉支付宝不要再发通知了
        } catch (Exception e) {
            log.error("回调处理失败", e);
            return "failure"; // 业务处理失败，告诉支付宝稍后重试
        }
    }
}