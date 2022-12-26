package com.cloud.gatewayserver.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description
 * @Author HeLiSen
 * @Date 2022/10/9 16:55
 */
@Component
@Slf4j
public class FilterOne implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("经过自定义过滤器");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -200;
    }
}
