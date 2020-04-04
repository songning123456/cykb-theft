package com.sn.theft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class TheftApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheftApplication.class, args);
    }

}
