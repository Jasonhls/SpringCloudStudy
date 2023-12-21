package com.cloud.clientserver;

import com.cloud.clientserver.config.loadbalancer.CustomLoadBalancerConfiguration;
import io.micrometer.core.instrument.MeterRegistry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * 也可以用@EnableEurekaClient
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(value = "com.cloud.clientserver.**.mapper")
@EnableFeignClients(value = {"com.cloud.testclient.api", "com.cloud.clientserver.feign"})
//指定调用某个服务，使用某个负载均衡的配置类
//@LoadBalancerClient(value = "test-client", configuration = CustomLoadBalancerConfiguration.class)
//调用所有服务，使用指定的负载均衡的配置类
@LoadBalancerClients(defaultConfiguration = CustomLoadBalancerConfiguration.class)
public class ClientServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientServerApplication.class, args);
    }

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }
}
