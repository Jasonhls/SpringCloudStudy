package com.cloud.testclient.limiter.guava;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

public class SmoothWarningUpTest {
    public static void main(String[] args) {
        /**
         * 第二个参数3表示热身时间为3秒
         * 第一个请求还是立即得到令牌，但是后面的请求和上面平滑突发限流就完全不一样了，按理来说 500 毫秒就会生成一个令牌，
         * 但是我们发现第二个请求却等了 1.3s，而不是 0.5s，后面第三个和第四个请求也等了一段时间。不过可以看出，
         * 等待时间在慢慢的接近 0.5s，直到第五个请求等待时间才开始变得正常。从第一个请求到第五个请求，
         * 这中间的时间间隔就是热身阶段，可以算出热身的时间就是我们设置的 3 秒
         *
         * 第一个参数还是每秒创建的令牌数量，这里是每秒 2 个，也就是每 500 毫秒生成一个，
         * 第二个参数表示从冷启动速率过渡到平均速率的时间间隔，也就是所谓的热身时间间隔（warm up period）
         */
        RateLimiter rateLimiter = RateLimiter.create(2, 3, TimeUnit.SECONDS);
//        0.0
//        1.331596
//        0.987043
//        0.665695
//        0.498767
//        0.499798
//        0.492755
//        0.498716
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());

    }
}
