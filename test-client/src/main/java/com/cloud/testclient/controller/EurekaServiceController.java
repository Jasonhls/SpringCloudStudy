package com.cloud.testclient.controller;

import cn.hutool.json.JSONUtil;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/eurekaService")
public class EurekaServiceController {

    @Autowired
    private SpringClientFactory springClientFactory;


    /**
     * 获取eureka的注册服务的实例列表
     * @return
     */
    @GetMapping(value = "/getServiceList")
    public List<String> getServiceList() {
        ILoadBalancer loadBalancer = springClientFactory.getLoadBalancer("test-client");
        ILoadBalancer loadBalancer1 = springClientFactory.getLoadBalancer("eureka-client");

        List<Server> allServers = loadBalancer.getAllServers();
        List<Server> reachableServers = loadBalancer.getReachableServers();

        List<Server> allServers1 = loadBalancer1.getAllServers();
        List<Server> reachableServers1 = loadBalancer1.getReachableServers();
        List<String> list = new ArrayList<>();
        list.add(getStr(allServers));
        list.add(getStr(reachableServers));
        list.add(getStr(allServers1));
        list.add(getStr(reachableServers1));
        return list;
    }

    private String getStr(List<Server> list) {
        Map<String, String> map = new HashMap<>(list.size());
        for (Server s : list) {
            map.put(s.getId(), JSONUtil.toJsonStr(s.getMetaInfo()));
        }
        return JSONUtil.toJsonStr(map);
    }
}


