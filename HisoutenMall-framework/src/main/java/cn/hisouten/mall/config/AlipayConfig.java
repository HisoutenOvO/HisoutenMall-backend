package cn.hisouten.mall.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝模拟支付配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig {
    private String appId;
    private String privateKey;
    private String alipayPublicKey;
    private String gatewayUrl;
    private String notifyUrl;
    private String returnUrl;

    @Bean
    public AlipayClient alipayClient() {
        // 注意：如果秘钥带空格或换行，SDK可能报错，建议确保秘钥是单行字符串
        return new DefaultAlipayClient(
                gatewayUrl, appId, privateKey,
                "json", "utf-8", alipayPublicKey, "RSA2"
        );
    }
}