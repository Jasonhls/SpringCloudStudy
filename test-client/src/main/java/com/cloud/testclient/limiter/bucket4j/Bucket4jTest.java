package com.cloud.testclient.limiter.bucket4j;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucket;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class Bucket4jTest {
    @Test
    public void a() {
        /**
         * 对于每分钟10个请求的速度限制，创建一个容量为10的存储桶，每分钟的重新填充速率为10个令牌
         */
        Refill refill = Refill.intervally(10, Duration.ofMillis(1));
        Bandwidth limit = Bandwidth.classic(10, refill);
        LocalBucket bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
        for (int i = 0; i <= 10; i++) {
            System.out.println(bucket.tryConsume(1));
        }
        bucket.tryConsume(1);
    }

    /**
     * Refill.interval在时间窗口的开始间隔地重新填充存储桶，设置重新填充速率为每2秒1个令牌，
     * 并限制我们的请求以遵守速率限制
     */
    @Test
    public void a2() {
        Bandwidth limit = Bandwidth.classic(1, Refill.intervally(1, Duration.ofSeconds(2)));
        LocalBucket bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
        System.out.println(bucket.tryConsume(1));
        Executors.newScheduledThreadPool(1)
                .schedule(() -> System.out.println(bucket.tryConsume(1)), 2, TimeUnit.SECONDS);
    }

    /**
     * 速率限制为每分钟10个请求，同时避免前5秒内耗尽所有令牌的峰值，Bucket4j允许在同一个存储桶上设置多个限制，
     * 如下添加另外一个限制，该限制在20秒的时间窗口内仅允许5个请求
     */
    @Test
    public void a3() {
        LocalBucket bucket = Bucket4j.builder()
                .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMillis(1))))
                .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofSeconds(20))))
                .build();
        for (int i = 1; i <= 5; i++) {
            System.out.println(bucket.tryConsume(1));
        }
        System.out.println(bucket.tryConsume(1));
    }
}
