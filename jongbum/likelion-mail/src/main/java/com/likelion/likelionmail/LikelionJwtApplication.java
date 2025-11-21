package com.likelion.likelionmail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {
        "com.likelion.likelionmail",                     // 기존 메인 패키지
        "com.likelion.likelionmail.global.config"        // SecurityConfig 패키지
})
public class LikelionJwtApplication {
    public static void main(String[] args) {
        SpringApplication.run(LikelionJwtApplication.class, args);
    }
}
