package com.fyasocialcapital.credits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CreditsApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CreditsApplication.class, args);
    }
}