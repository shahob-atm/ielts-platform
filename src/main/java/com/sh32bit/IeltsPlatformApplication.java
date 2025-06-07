package com.sh32bit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class IeltsPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(IeltsPlatformApplication.class, args);
    }

}
