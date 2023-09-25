package com.cloud.shardingjdbc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: 何立森
 * @Date: ${YEAR}/${MONTH}/${DAY}/${TIME}
 * @Description: 分库分表
 * 参考文档：http://wed.xjx100.cn/news/207017.html?action=onClick
 * 官方文档：https://shardingsphere.apache.org/document/legacy/3.x/document/cn/quick-start/sharding-jdbc-quick-start/
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(value = "com.cloud.shardingjdbc.mapper")
public class ShardingJdbcApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcApplication.class, args);
    }
}