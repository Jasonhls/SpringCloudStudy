package com.cloud.clientserver.controller;

import com.cloud.clientserver.feign.TestFeign;
import com.cloud.clientserver.pojo.InstanceVipAddressParam;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: helisen
 * @Date 2021/11/11 16:06
 * @Description:
 */
@RestController
@RequestMapping(value = "/hello")
public class HelloController {

    @Autowired
    private TestFeign testFeign;

    @GetMapping(value = "/sayHello")
    public String sayHello(@RequestParam(value = "name") String name) {
        return "hello, " + name;
    }

    @PostMapping(value = "/getString")
    public String getString() {
        //服务之间直接调用
        return testFeign.test();
    }

    /**
     * 根据服务名称获取服务实例信息
     * @param param
     * @return
     */
    @PostMapping(value = "/test")
    public List<InstanceInfo> test(@RequestBody InstanceVipAddressParam param) {
        //DiscoveryManager类已经过时
        DiscoveryClient discoveryClient = DiscoveryManager.getInstance().getDiscoveryClient();
        EurekaClientConfig eurekaClientConfig = eurekaClient.getEurekaClientConfig();
        List<InstanceInfo> defaultZone = discoveryClient.getInstancesByVipAddress(param.getVipAddress(), param.getSecure(), param.getRegion());
        return defaultZone;
    }

    @Autowired
    private EurekaClient eurekaClient;

    /**
     * 不过时根据实例名称获取服务实例信息的方法
     * @param param
     * @return
     */
    @PostMapping(value = "/test2")
    public List<InstanceInfo> test2(@RequestBody InstanceVipAddressParam param) {
        List<InstanceInfo> instancesByVipAddress = eurekaClient.getInstancesByVipAddress(param.getVipAddress(), param.getSecure(), param.getRegion());
        return instancesByVipAddress;

    }

    @PostMapping(value = "/synchronizeStringExample")
    public void synchronizeStringExample() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                String ss = "abcde1234rdsfsd";
                //随机字符串+数字
                String s = ss + "";
                lockString(s);
            });

        }
        executorService.shutdown();
    }

    public static void lockString(String s) {
        //这里必须是s.intern()方法，不能直接用s，s可能每次传过来的对象是不同的对象
        //String类的intern()方法，会判断该字符串是否存在常量池中，如果存在直接获取，不存在就将当前字符串放入到常量池中
        //大量的业务数据，不能这么做，因为常量池在元空间，会把元空间占满，所以还是用redis加锁
        synchronized (s.intern()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(new Date() + " " + s);
        }
    }
}
