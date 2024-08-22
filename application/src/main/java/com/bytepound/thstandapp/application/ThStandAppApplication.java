package com.bytepound.thstandapp.application;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.bytepound.thstandapp")
@MapperScan("com.bytepound.thstandapp.common.repository.mysql.dao")
public class ThStandAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThStandAppApplication.class, args);
        log.info("MainApplication server start up success");
    }

}
