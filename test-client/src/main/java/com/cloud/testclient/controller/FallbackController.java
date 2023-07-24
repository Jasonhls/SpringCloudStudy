package com.cloud.testclient.controller;

import com.cloud.testclient.api.HelloFeignClient;
import com.cloud.testclient.req.StudentReq;
import com.cloud.testclient.resp.Student;
import com.cloud.testclient.service.CircuitBreakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * @Author: helisen
 * @Date 2021/11/11 15:49
 * @Description:
 */
@RestController
@RequestMapping(value = "/fallback")
public class FallbackController {

    @Autowired
    private HelloFeignClient helloFeignClient;

    @GetMapping(value = "/get")
    public Mono<String> fallback() {
        return Mono.just("service error, jump fallback");
    }

    @Autowired
    HystrixCommandController hystrixCommandController;

    @GetMapping(value = "/addStudent")
    public Student addStudent() {
        StudentReq req = new StudentReq();
        req.setAge(10);
        req.setName("hello");
        Student student = helloFeignClient.getStudent(req);
        return student;
    }


    @GetMapping(value = "/testThreadFallback")
    public void testThreadFallback() throws InterruptedException {
        hystrixCommandController.threadFallback();
        System.out.println("开始时间： " + new Date());
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        hystrixCommandController.thread();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        Thread.sleep(10000);
        System.out.println("完成了");
    }


    @GetMapping(value = "/circuitBreaker/{num}")
    public String circuitBreaker(@PathVariable(value = "num") int num) {
        return new CircuitBreakerService(num).execute();
    }
}
