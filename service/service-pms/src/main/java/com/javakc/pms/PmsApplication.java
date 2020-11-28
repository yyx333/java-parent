package com.javakc.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ComponentScan(basePackages = {"com.javakc"})
@SpringBootApplication
@EnableJpaAuditing
@EnableDiscoveryClient
public class PmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmsApplication.class,args);
    }

}