package com.cloud.clientserver.limit;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: 何立森
 * @Date: 2024/07/30/15:17
 * @Description:
 */
public class LeakyBucketRateLimiter {
    //当前桶中的请求数量
    private AtomicInteger bucketLevel;
    //桶的容量
    private int capacity;
    //漏水速率，单位：请求/秒
    private long leakRate;
    //上一次漏水的时间戳
    private long lastLeakTime;

    public LeakyBucketRateLimiter(int capacity, long leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.bucketLevel = new AtomicInteger(0);
        this.lastLeakTime = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire() {
        //获取当前时间
        long currentTime = System.currentTimeMillis();
        //流出时间
        long elapsedTime = currentTime - lastLeakTime;
        //计算流出的水量 = (当前时间 - 上次时间) * 出水速率
        long leaked = (long) (elapsedTime * (leakRate / 1000.0));

        //只有有流出水才更新时间戳，不然会漏不出水
        if(leaked > 0) {
            //计算桶内水量 = 桶内当前水量 - 流出水量
            int newLevel = Math.max(0, bucketLevel.get() - (int) leaked);
            bucketLevel.set(newLevel);

            //更新上次漏水时间戳
            lastLeakTime = currentTime;
        }

        //尝试将请求加入桶中
        if(bucketLevel.get() < capacity) {
            bucketLevel.incrementAndGet();
            return true;
        }else {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        //容量为1，漏水速率为 1请求/秒
        LeakyBucketRateLimiter limiter = new LeakyBucketRateLimiter(1,1);
        //模拟发送请求
        for (int i = 0; i < 20; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(limiter.tryAcquire()) {
                        System.out.println(Thread.currentThread().getName() + "获得了许可，执行操作。");
                    }else {
                        System.out.println(Thread.currentThread().getName() + "请求被拒绝。");
                    }
                }
            }).start();
            //模拟执行时间
            Thread.sleep(600);
        }
    }
}
