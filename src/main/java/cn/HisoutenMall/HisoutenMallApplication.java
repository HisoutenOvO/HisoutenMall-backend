package cn.HisoutenMall;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class HisoutenMallApplication {
    public static void main(String[] args) {
        SpringApplication.run(HisoutenMallApplication.class, args);
        log.info("HisoutenMall项目启动成功!");
    }
}