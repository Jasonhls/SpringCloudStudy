package com.cloud.testclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableCircuitBreaker //开启熔断器的开关
@EnableFeignClients(value = "com.cloud.testclient.api")
public class TestClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestClientApplication.class, args);
    }

}
