package com.cloud.canal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: 何立森
 * @Date: ${YEAR}/${MONTH}/${DAY}/${TIME}
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CanalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CanalApplication.class, args);
    }
}