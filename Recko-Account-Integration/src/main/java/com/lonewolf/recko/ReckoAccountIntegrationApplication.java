package com.lonewolf.recko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReckoAccountIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReckoAccountIntegrationApplication.class, args);
    }
}
