package cn.hisouten.mall.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cn.hisouten.mall")
@Slf4j
@MapperScan(basePackages = "cn.hisouten.mall", annotationClass = Mapper.class)
public class HisoutenMallServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HisoutenMallServerApplication.class, args);
        log.info("HisoutenMall启动完毕！");
    }
}
