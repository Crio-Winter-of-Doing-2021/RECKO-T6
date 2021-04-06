package com.lonewolf.recko;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Slf4j
public class ReckoAccountIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReckoAccountIntegrationApplication.class, args);
    }

    @PostConstruct
    public void applicationInitialized() {
        log.info("========= spring boot application is working properly =========");
    }
}
