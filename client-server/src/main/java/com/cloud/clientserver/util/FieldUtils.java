package com.cloud.clientserver.util;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class FieldUtils {

    private static final Map<String, Class> FILED_CACHE = new ConcurrentHashMap<>(128);

    /**
     * 根据属性名称获取属性类型
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Class<?> getFiledType(Class clazz, String fieldName) {
        String key = clazz.getName() + ":" + fieldName;
        Class cache = FILED_CACHE.get(key);
        if (cache != null) {
            return cache;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (StringUtils.equals(fieldName, field.getName())) {
                FILED_CACHE.put(key, field.getType());
                return field.getType();
            }
        }
        return null;
    }

    /**
     * 判断属性是否为数值，mongo查询没有隐式转换
     * @param clazz
     * @param fieldName
     * @return
     */
    public static boolean isNumberField(Class clazz, String fieldName) {
        Class<?> type = getFiledType(clazz, fieldName);
        return type != null && (Integer.class == type);
    }

    public static boolean isIntegerField(Class clazz, String fieldName) {
        Class<?> type = getFiledType(clazz, fieldName);
        return Integer.class == type;
    }

    public static boolean isLongField(Class clazz, String fieldName) {
        Class<?> type = getFiledType(clazz, fieldName);
        return Long.class == type;
    }

    public static boolean isBigDecimalField(Class clazz, String fieldName) {
        Class<?> type = getFiledType(clazz, fieldName);
        return BigDecimal.class == type;
    }

    public static boolean isBooleanField(Class clazz, String fieldName) {
        Class<?> type = getFiledType(clazz, fieldName);
        return Boolean.class == type;
    }

    public static boolean isListField(Class clazz, String fieldName) {
        Class<?> type = getFiledType(clazz, fieldName);
        return List.class == type;
    }

}
