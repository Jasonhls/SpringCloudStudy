package com.cloud.gatewayserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;

/**
 * @Author: 何立森
 * @Date: 2023/12/26/17:34
 * @Description: 手动动态的增加删除路由
 */
@RequestMapping(value = "/route")
@RestController
public class RouteController {
    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    @PostMapping(value = "/add")
    public void addRoute(String id, String uri, String predicates) {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(id);
        routeDefinition.setUri(URI.create(uri));
        routeDefinition.setPredicates(Collections.singletonList(new PredicateDefinition(predicates)));
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
    }

    @PostMapping(value = "/update")
    public void updateRoute(String id, String uri, String predicates) {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(id);
        routeDefinition.setUri(URI.create(uri));
        routeDefinition.setPredicates(Collections.singletonList(new PredicateDefinition(predicates)));
        routeDefinitionWriter.delete(Mono.just(routeDefinition.getId())).then(Mono.defer(() -> {
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            return Mono.empty();
        })).subscribe();
    }

    @PostMapping(value = "/delete")
    public void deleteRoute(String id) {
        routeDefinitionWriter.delete(Mono.just(id)).subscribe();
    }
}
