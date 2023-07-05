package com.cloud.es.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: 何立森
 * @Date: 2023/04/04/9:10
 * @Description:
 */
@RestController
@RequestMapping(value = "/syncTest")
@Slf4j
public class SyncTestController {
    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 测试traceId是否有透传到线程池异步代码中
     */
    @PostMapping(value = "/thread")
    public void syncMethod() {
        log.info("主线程方法开始执行");
        threadPoolTaskExecutor.execute(() -> {
            log.info("线程池执行异步方法");
        });
        log.info("主线程方法执行结束");
    }

}
