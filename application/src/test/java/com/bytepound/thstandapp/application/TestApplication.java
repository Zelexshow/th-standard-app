package com.bytepound.thstandapp.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class TestApplication {

    private static final Logger log = LoggerFactory.getLogger(TestApplication.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(TestApplication.class);
        ApplicationPidFileWriter pidFileWriter = new ApplicationPidFileWriter();
        pidFileWriter.setTriggerEventType(ApplicationReadyEvent.class);
        application.addListeners(pidFileWriter);
        application.addListeners((ApplicationListener<ApplicationFailedEvent>) event -> {
            log.info("--------------------ApplicationFailedEvent-----------------------");
            log.error("application error", event.getException());
        });
        application.run(args);
        log.info("server start up success");
    }

}
