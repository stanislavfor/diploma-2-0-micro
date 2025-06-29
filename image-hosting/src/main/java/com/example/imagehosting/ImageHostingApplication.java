package com.example.imagehosting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Отключение стандартной Spring Security
        (exclude = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
        })

public class ImageHostingApplication {
    public static void main(String[] args) {

        SpringApplication.run(ImageHostingApplication.class, args);
    }

}
