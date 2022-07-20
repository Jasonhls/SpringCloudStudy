package com.cloud.testclient.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.springframework.stereotype.Service;

@Service
public class CircuitBreakerService extends HystrixCommand<String> {

    private int num;

    CircuitBreakerService() {
        super(CIRCUITBREAKER);
    }

    public CircuitBreakerService(int num) {
        super(CIRCUITBREAKER);
        this.num = num;
    }

    //熔断降级
    static HystrixCommand.Setter CIRCUITBREAKER = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HystrixService"))
            .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                .withCircuitBreakerEnabled(true)
                .withCircuitBreakerRequestVolumeThreshold(5)
                .withCircuitBreakerErrorThresholdPercentage(50)
                .withCircuitBreakerSleepWindowInMilliseconds(5000)
            );

    @Override
    protected String run() throws Exception {
        if(num % 2 == 0) {
            return "正常访问";
        }
        throw new RuntimeException("");
    }

    @Override
    protected String getFallback() {
        return "熔断降级";
    }

    //超时降级
    static HystrixCommand.Setter TIMEOUT = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HystrixService"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                .withExecutionTimeoutEnabled(true)
                .withExecutionTimeoutInMilliseconds(3000)
            );

    //线程池隔离
    static HystrixCommand.Setter THREAD = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HystrixService"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                .withCoreSize(20)
                .withMaxQueueSize(1000)
                .withQueueSizeRejectionThreshold(800)
                .withKeepAliveTimeMinutes(2)
            );

    //信号量隔离
    static HystrixCommand.Setter SEMAPHORE = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HystrixService"))
            .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                .withExecutionIsolationSemaphoreMaxConcurrentRequests(2)
            );
}
