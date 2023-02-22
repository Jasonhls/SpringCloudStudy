package com.cloud.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: 何立森
 * @Date: ${YEAR}/${MONTH}/${DAY}/${TIME}
 * @Description:
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MqServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MqServiceApplication.class, args);
    }
}