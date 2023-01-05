package com.cloud.testclient.limiter.guava;

import com.google.common.util.concurrent.RateLimiter;

public class SmoothBurstyTest {
    public static void main(String[] args) {
        //表示这个限流器容量为5，并且每秒生成5个令牌，也就是每隔200毫秒生成一个
        RateLimiter rateLimiter = RateLimiter.create(5);
        //消费令牌，如果桶中令牌足够，返回0，如果令牌不足，则阻塞等待，并返回等待时间。
        //默认为1，表示一秒钟一个请求过来
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());


        RateLimiter rateLimiter2 = RateLimiter.create(5);
        //模拟突发一秒钟10个请求同时过来
        System.out.println(rateLimiter2.acquire(10));
        System.out.println(rateLimiter2.acquire());
        System.out.println(rateLimiter2.acquire());
        System.out.println(rateLimiter2.acquire());
        System.out.println(rateLimiter2.acquire());

    }


}
