package com.cloud.shardingjdbc;

import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan(value = "com.cloud.shardingjdbc.mapper")
public class ShardingJdbcApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcApplication.class, args);
    }
}