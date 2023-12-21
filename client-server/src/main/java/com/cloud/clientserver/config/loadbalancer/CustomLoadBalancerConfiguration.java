package com.cloud.clientserver.config.loadbalancer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 何立森
 * @Date: 2023/12/21/15:45
 * @Description: 自定义负载均衡配置类
 * 还需要在启动类上面添加注解：@LoadBalancerClient(value = "test-client", configuration = CustomLoadBalancerConfiguration.class)
 */
@Configuration
public class CustomLoadBalancerConfiguration {
    /**
     * 注入自定义的ReactorServiceInstanceLoadBalancer
     * @param serviceInstanceListSupplierObjectProvider 服务列表
     * @return
     */
    @Bean
    public ReactorServiceInstanceLoadBalancer reactorServiceInstanceLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider) {
        return new CustomLoadBalancer(serviceInstanceListSupplierObjectProvider);
    }
}
