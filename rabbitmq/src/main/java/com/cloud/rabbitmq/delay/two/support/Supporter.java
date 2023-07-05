package com.cloud.rabbitmq.delay.two.support;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.text.StrFormatter;
import com.cloud.mq.delayqueue.rabbitmq.two.converter.DelayMsgConverter;
import com.cloud.mq.delayqueue.rabbitmq.two.vo.DelayMsg;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

import static com.cloud.mq.delayqueue.rabbitmq.two.consts.Consts.VERSION;
import static java.util.Objects.requireNonNull;

/**
 * 消息序列化器
 */
@Slf4j
public class Supporter {
    private static final String REDIS_KEY = "delayMsg:{}:{}";

    private Supporter() {
    }

    /**
     * 根据消息对象得到redis-key
     */
    public static String getRedisKey(@NotBlank String appName, @NonNull DelayMsg<?> msg) {
        return StrFormatter.format(REDIS_KEY, appName, msg.getIdentity());
    }

    /**
     * 使用指定版本号序列化消息体
     */
    @SneakyThrows
    public static <T> String serializeWithVersion(@NonNull T data, int version) {
        return DelayMsgConverter.serializeWithAppendValues(data, Pair.of(VERSION, version));
    }

    /**
     * 从消息体中获取version
     */
    @SneakyThrows
    public static int readVersion(@NotBlank String valueString) {
        return Integer.parseInt(requireNonNull(DelayMsgConverter.readAppendValue(valueString, VERSION).get(VERSION)));
    }
}
