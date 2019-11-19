package com.example.thread_pool_executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableEurekaClient
@EnableFeignClients
@EnableResourceServer
@EnableEurekaServer
@RefreshScope
public class ThreadPoolExecutorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThreadPoolExecutorApplication.class, args);
    }

}
