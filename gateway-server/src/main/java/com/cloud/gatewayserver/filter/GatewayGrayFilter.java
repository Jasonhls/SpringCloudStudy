//package com.cloud.gatewayserver.filter;
//
//import com.cloud.gatewayserver.constant.GrayConstant;
//import com.cloud.gatewayserver.gray.GrayRequestContextHolder;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
///**
// * @Description 灰度过滤器
// * @Author HeLiSen
// * @Date 2022/12/9 9:20
// */
//@Component
//public class GatewayGrayFilter implements GlobalFilter {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        //解析请求头，查看是否存在灰度发布的请求头信息，如果存在则将其放置到ThreadLocal中
//        HttpHeaders headers = exchange.getRequest().getHeaders();
//        if(headers.containsKey(GrayConstant.GRAY_HEADER)) {
//            String gray = headers.getFirst(GrayConstant.GRAY_HEADER);
//            if(GrayConstant.GRAY_VALUE.equals(gray)) {
//                //设置灰度标识
//                GrayRequestContextHolder.setGrayTag(true);
//            }
//        }
//        //将灰度标识放入请求头中
//        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
//                //将灰度标记传递过去
//                .header(GrayConstant.GRAY_HEADER, GrayRequestContextHolder.getGrayTag().toString())
//                .build();
//        ServerWebExchange serverWebExchange = exchange.mutate().request(serverHttpRequest).build();
//        return chain.filter(serverWebExchange);
//    }
//}
