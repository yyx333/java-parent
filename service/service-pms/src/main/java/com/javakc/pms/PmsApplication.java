package com.javakc.pms;

//@EnableJpaAuditing
//@SpringBootApplication

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ComponentScan(basePackages = {"com.javakc"})
@SpringBootApplication
@EnableJpaAuditing
public class PmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmsApplication.class,args);
    }

}