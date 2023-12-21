package com.cloud.clientserver.config.loadbalancer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

/**
 * @Author: 何立森
 * @Date: 2023/12/21/14:37
 * @Description: 自定义负载均衡配置器
 */
@Component
@Slf4j
public class CustomLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    /**
     * 服务列表
     */
    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider;

    public CustomLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider) {
        this.serviceInstanceListSupplierObjectProvider = serviceInstanceListSupplierObjectProvider;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierObjectProvider.getIfAvailable();
        return supplier.get().next().map(this::getInstanceResponse);
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        if(instances.isEmpty()) {
            return new EmptyResponse();
        }
        int size = instances.size();
        int index = new Random().nextInt(size);
        log.info("自定义负载均衡器：{}，选中的是：{}", size, index);
        ServiceInstance serviceInstance = instances.get(index);
        return new DefaultResponse(serviceInstance);
    }
}
