package com.cloud.clientserver.limit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2024/07/30/16:04
 * @Description:
 */
@Slf4j
public class TokenBucketRateLimiter {

    private static final String KEY_PREFIX = "TokenRateLimiter";

    RedisTemplate redisTemplate;


    public TokenBucketRateLimiter(RedisTemplate redisTemplate, int permitsPerSecond, String bucketKey) {
        //初始化构造信息
        List<String> keys = new ArrayList<>();
        keys.add(getRateLimiterKey(bucketKey));
        this.redisTemplate = redisTemplate;
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Boolean.class);
        redisScript.setLocation(get("lua/rateLimitInit.lua"));
        this.redisTemplate.execute(redisScript, keys, String.valueOf(permitsPerSecond), String.valueOf(permitsPerSecond));
    }


    //构造令牌桶缓存key
    public String getRateLimiterKey(String bucketKey) {
        return KEY_PREFIX + bucketKey;
    }

    public boolean tryAcquire(String bucketKey) {
        return tryAcquire(1, bucketKey);
    }

    public boolean tryAcquire(int request, String bucketKey) {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Boolean.class);
        redisScript.setLocation(get("lua/rateLimit.lua"));
        List<String> keys = new ArrayList<>();
        keys.add(getRateLimiterKey(bucketKey));
        return (boolean) this.redisTemplate.execute(redisScript, keys, String.valueOf(request), String.valueOf(System.currentTimeMillis() / 1000));
    }

    public Resource get(String path) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        URL resource = contextClassLoader.getResource(path);
        return new FileUrlResource(resource);
    }
}
