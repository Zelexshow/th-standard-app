package com.bytepound.thstandapp.server;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.bytepound.thstandapp")
@MapperScan("com.bytepound.thstandapp.business.repository.mysql.dao")
public class ThStandAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThStandAppApplication.class, args);
        log.info("MainApplication server start up success");
    }

}
