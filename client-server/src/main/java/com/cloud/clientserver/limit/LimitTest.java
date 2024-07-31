package com.cloud.clientserver.limit;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Author: 何立森
 * @Date: 2024/07/30/16:29
 * @Description:
 */
@RequestMapping(value = "/limitTest")
@RestController
@Slf4j
public class LimitTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping(value = "/testTokenBucketRateLimiter")
    public void testTokenBucketRateLimiter() throws Exception {
        //令牌桶
        String bucketKey = "yyfsTest";

        //每秒新增3个桶
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(redisTemplate, 3, bucketKey);

        //模拟发送请求
        int allNum = 4;
        for (int i = 0; i < allNum; i++) {
            if(limiter.tryAcquire(bucketKey)) {
                System.out.println(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " 获得了许可，执行操作。");
            }else {
                System.out.println(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " 请求被拒绝。");
            }
        }

        Thread.sleep(5000);

        allNum = 20;
        for (int i = 0; i < allNum; i++) {
            if(limiter.tryAcquire(bucketKey)) {
                System.out.println(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " 获得了许可，执行操作。");
            }else {
                System.out.println(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " 请求被拒绝。");
            }
        }

    }

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping(value = "/sendMessage")
    public void sendMessage(String phone) {
        if(StringUtils.isNotBlank(phone)) {
            RRateLimiter rateLimiter = redissonClient.getRateLimiter("rate_limiter_" + phone);
            //每10秒产生1个令牌
            rateLimiter.trySetRate(RateType.OVERALL, 1, 10, RateIntervalUnit.SECONDS);

            if(rateLimiter.tryAcquire(1)) {
                log.info("向手机{}发送短信", phone);
            }
        }
    }
}
