package cn.hisouten.mall.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "cn.hisouten.mall")
@Slf4j
@MapperScan(basePackages = "cn.hisouten.mall", annotationClass = Mapper.class)
@EnableTransactionManagement
@EnableScheduling
@EnableCaching
public class HisoutenMallServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HisoutenMallServerApplication.class, args);
        log.info("HisoutenMall启动完毕！");
    }
}
