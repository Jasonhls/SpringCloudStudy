package com.cloud.rabbitmq.delay.one;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author sha.li
 * Redis工具类<br/>
 * 无构造函数，静态调用<br/>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Slf4j
public class RedisUtils {

    @Getter
    private static RedisTemplate<String, Object> redisTemplate;

    /**
     * 使用scan模糊查询key时的处理批次数量
     */
    private static final int SCAN_BATCH_SIZE = 1000;

    private RedisUtils() {
    }

    public static void init(RedisTemplate<String, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    public static boolean hasKey(@NonNull String key) {
        Boolean hasKey = redisTemplate.hasKey(key);
        return BooleanUtil.isTrue(hasKey);
    }

    public static boolean hHasKey(@NonNull String key, @NonNull Object hashKey) {
        Boolean hasKey = redisTemplate.opsForHash().hasKey(key, hashKey);
        return BooleanUtil.isTrue(hasKey);
    }

    /**
     * 获取所有的键
     */
    public static Set<Object> hGetKeys(@NonNull String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(@NonNull String key, long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(@NonNull String key, long timeout, TimeUnit unit) {
        Boolean ret = redisTemplate.expire(key, timeout, unit);
        return BooleanUtil.isTrue(ret);
    }

    /**
     * @param key  Redis键
     * @param unit 时间单位
     * @return 剩余时间输
     */

    public static Long getExpire(@NonNull String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 删除单个key
     *
     * @param key 键
     * @return true=删除成功；false=删除失败
     */
    public static boolean del(@NonNull String key) {
        Boolean ret = redisTemplate.delete(key);
        return BooleanUtil.isTrue(ret);
    }

    /**
     * 删除多个key
     *
     * @param keys 键集合
     * @return 成功删除的个数
     */
    public static long del(Collection<String> keys) {
        Long ret = redisTemplate.delete(keys);
        return ret == null ? 0 : ret;
    }

    /**
     * 模糊查询获取key值  删除
     * @param pattern 前缀key
     * @return Boolean
     */
    public static Boolean deleteAllKeysByPrefixPattern(String pattern) {
        del(scanMatch(pattern));
        return Boolean.TRUE;
    }


    /**
     * 使用scan 获取所有key
     * @param pattern 要模糊查询的key匹配字符串，需包含 <br>*</b> 符号，否则会成为精确匹配
     * @return Set<String>
     */
    public static Set<String> scanMatch(String pattern) {
        log.info("redis searching for pattern {}", pattern);
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keys = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(pattern).count(SCAN_BATCH_SIZE).build());
            if (cursor.hasNext())
                cursor.forEachRemaining(bs -> keys.add(new String(bs)));
            return keys;
        });
    }

    /**
     * 存入普通对象
     *
     * @param key   Redis键
     * @param value 值
     */
    public static void set(@NonNull String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * redis中没有对应key时存入普通对象
     *
     * @param key      键
     * @param value    值
     * @param timeout  有效期
     * @param timeUnit 时间单位
     */
    public static Boolean setIfAbsent(@NonNull String key, Object value, long timeout, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }

    /**
     * redis中没有对应key时存入普通对象
     *
     * @param key     键
     * @param value   值
     * @param timeout 有效期，单位秒
     */
    public static Boolean setIfAbsent(@NonNull String key, Object value, long timeout) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * redis中没有对应key时存入普通对象
     *
     * @param key   键
     * @param value 值
     */
    public static Boolean setIfAbsent(@NonNull String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 存入普通对象
     *
     * @param key     键
     * @param value   值
     * @param timeout 有效期，单位秒
     */
    public static void set(@NonNull String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 存入普通对象
     *
     * @param key      键
     * @param value    值
     * @param duration 时间区间
     */
    public static void set(@NonNull String key, Object value, Duration duration) {
        set(key, value, duration.getSeconds());
    }

    public static void set(@NonNull String key, @NonNull Object value, long timeout,
                           TimeUnit timeUnit) {
        if (timeout < 1) {
            set(key, value);
        }
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public static void setAndDefaultTimeOut(@NonNull String key, @NonNull Object value) {
        set(key, value, 1, TimeUnit.DAYS);
    }

    // 存储普通对象操作

    /**
     * 获取json对象
     *
     * @param key 键
     * @return 对象
     */
    @Nullable
    public static Object get(@NonNull String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public static void hPut(@NonNull String key, Object hKey, Object value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    // 存储Hash操作

    /**
     * 往Hash中存入多个数据
     *
     * @param key    Redis键
     * @param values Hash键值对
     */
    public static void hPutAll(@NonNull String key, Map<?, ?> values) {
        if (MapUtil.isEmpty(values)) {
            return;
        }
        redisTemplate.opsForHash().putAll(key, values);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public static Object hGet(@NonNull String key, Object hKey) {
        return redisTemplate.opsForHash().get(key, hKey);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键
     * @return Hash中的对象
     */
    public static List<?> hGet(@NonNull String key, Collection<? super Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键
     * @return Hash中的对象
     */
    public static List<Object> hGet(@NonNull String key, Object[] hKeys) {
        List<Object> hashKeys = Arrays.stream(hKeys).map(Object::toString).collect(Collectors.toList());
        List<Object> objects = redisTemplate.opsForHash().multiGet(key, hashKeys);
        if (objects.size() == 1 && objects.get(0) == null) {
            return null;
        }
        return objects;
    }




}
