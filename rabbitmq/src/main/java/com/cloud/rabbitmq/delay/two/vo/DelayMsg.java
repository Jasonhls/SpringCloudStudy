package com.cloud.rabbitmq.delay.two.vo;

import cn.hutool.core.util.HashUtil;
import com.cloud.mq.delayqueue.rabbitmq.two.consts.ConflictStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import java.time.Duration;

import static cn.hutool.core.text.CharPool.COLON;
import static com.cloud.mq.delayqueue.rabbitmq.two.converter.DelayMsgConverter.serialize;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

/**
 * 延时消息负载主体
 */
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class DelayMsg<T> {
    /**
     * 消息主题，供订阅使用
     */
    private String topic;
    /**
     * 消息内容
     */
    private T data;

    /**
     * 延迟时间
     */
    private Duration delay;

    /**
     * 消息冲突时的处理策略
     */
    private ConflictStrategy conflictStrategy = ConflictStrategy.EXCEPTION;

    @Getter(AccessLevel.NONE)
    @JsonIgnore
    private String cachedHashValue;

    public DelayMsg(@NotBlank String topic, @NonNull T data, @NonNull Duration delay) {
        this.topic = topic;
        this.data = data;
        this.delay = delay;
    }

    public DelayMsg(@NotBlank String topic, @NonNull T data, @NonNull Duration delay, @NonNull ConflictStrategy conflictStrategy) {
        this.topic = topic;
        this.data = data;
        this.delay = delay;
        this.conflictStrategy = conflictStrategy;
    }


    /**
     * 检查参数
     */
    public void checkValid() {
        Assert.isTrue(hasText(topic), "DelayMsg topic should not be blank");
        Assert.isTrue(nonNull(data), "DelayMsg data should not be null");
        Assert.isTrue(nonNull(delay), "DelayMsg delay should not be null");
    }

    /**
     * 获取唯一标识ID
     */
    @JsonIgnore
    public String getIdentity() {
        if (null == cachedHashValue)
            cachedHashValue = topic + COLON + Math.abs(HashUtil.cityHash64(serialize(data).getBytes()));
        return cachedHashValue;
    }

    /**
     * 获取延迟毫秒数
     */
    @JsonIgnore
    public long getDelayMs() {
        return delay.toMillis() < 0 ? 0 : delay.toMillis();
    }

    @JsonIgnore
    public String toString() {
        return serialize(this);
    }
}
