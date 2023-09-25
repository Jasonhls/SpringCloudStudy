package com.cloud.shardingjdbc.utils;

import java.util.Map;

/**
 * @Author: 何立森
 * @Date: 2023/09/25/16:12
 * @Description:
 */
public class ThreadLocalUtils {
    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public static void set(String key, Object value) {
        remove(key);
        threadLocal.get().put(key, value);
    }

    public static Object get(String key) {
        Object value = threadLocal.get().get(key);
        remove(key);
        return value;
    }

    public static void removeAll() {
        threadLocal.remove();
    }

    public static void remove(String key) {
        threadLocal.get().remove(key);
    }

}
