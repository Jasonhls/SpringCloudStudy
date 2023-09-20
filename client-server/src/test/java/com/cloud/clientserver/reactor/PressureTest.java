package com.cloud.clientserver.reactor;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.resource.DefaultClientResources;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: 何立森
 * @Date: 2023/07/31/17:33
 * @Description:
 */
@SpringBootTest
public class PressureTest {

    public final RedisURI redisUrl = RedisURI.builder().withHost("127.0.0.1").withPort(6379).build();

    /**
     * 同步
     * @throws InterruptedException
     */
    @Test
    public void sync() throws InterruptedException {
        //请求处理线程10个
        ExecutorService pool = Executors.newFixedThreadPool(10);
        RedisClient redisClient = RedisClient.create(redisUrl);
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        long startTime = System.currentTimeMillis();
        //模拟30个请求
        for (int i = 0; i < 30; i++) {
            pool.execute(() -> {
                //同步获取
                String favorite = connect.sync().get("a");
                System.out.println("获取的value为：" + favorite);
            });
        }

        pool.execute(() -> {
            //执行到这里代表线程池的线程都释放出来了，可以做其他事情了，记录一下时间
            long endTime = System.currentTimeMillis();
            System.out.println("free:" + (endTime - startTime));
        });

        while(true) {
            Thread.sleep(10000);
        }
    }

    /**
     * 异步
     */
    @Test
    public void async() {
        //请求处理线程5个
        ExecutorService pool = Executors.newFixedThreadPool(5);
        DefaultClientResources.builder().ioThreadPoolSize(5)

    }

}
