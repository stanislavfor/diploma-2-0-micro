package com.example.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Отключение стандартной Spring Security
        (exclude = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
        })

public class FrontendServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(FrontendServiceApplication.class, args);
    }

}
