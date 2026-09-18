package cn.hisouten.mall.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class HisoutenMallServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HisoutenMallServerApplication.class, args);
        log.info("HisoutenMall启动完毕！");
    }
}
