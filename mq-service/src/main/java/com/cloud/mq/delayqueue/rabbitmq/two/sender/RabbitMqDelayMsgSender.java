package com.cloud.mq.delayqueue.rabbitmq.two.sender;

import com.cloud.mq.delayqueue.rabbitmq.two.config.DelayMsgProperties;
import com.cloud.mq.delayqueue.rabbitmq.two.exception.DelayMsgProduceFailedException;
import com.cloud.mq.delayqueue.rabbitmq.two.exception.MsgConflictException;
import com.cloud.mq.delayqueue.rabbitmq.two.support.Supporter;
import com.cloud.mq.delayqueue.rabbitmq.two.vo.DelayMsg;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import java.time.Duration;

import static com.cloud.mq.delayqueue.rabbitmq.two.converter.DelayMsgConverter.serialize;
import static com.cloud.mq.delayqueue.rabbitmq.two.support.Supporter.serializeWithVersion;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.singletonMap;
import static java.util.Objects.requireNonNull;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Slf4j
public class RabbitMqDelayMsgSender extends AbstractDelayMsgSender {
    @Getter(AccessLevel.PACKAGE)
    private final AmqpTemplate amqpTemplate;
    private final RedisTemplate<String,Object> redisTemplate;
    private final DelayMsgProperties properties;
    @Value("${spring.application.name}")
    private String appName;
    private final String queueName;

    public RabbitMqDelayMsgSender(AmqpTemplate amqpTemplate, DelayMsgProperties properties,
            RedisTemplate<String,Object> redisTemplate, String appName, String queueName) {
        this.appName = appName;
        this.properties = properties;
        this.redisTemplate = redisTemplate;
        this.amqpTemplate = amqpTemplate;
        this.queueName = queueName;
    }

    @NonNull
    @Override
    public String currentAppName() {
        return appName;
    }

    @Override
    public void sendTo(@NotBlank String appName, @NonNull DelayMsg<?> msg) {
        if (log.isDebugEnabled()) {
            log.debug("Ready to sending msg:{} to {}...", msg, appName);
        }
        Assert.isTrue(hasText(appName), "AppName should not be blank");
        msg.checkValid();
        if(msg.getDelay().toMillis() >= properties.getAllowedSendMaxDelay().toMillis())
            throw new DelayMsgProduceFailedException("请求的延迟时长超出最大限制[" + properties.getAllowedSendMaxDelay() + "]");
        doSend(appName, msg);
    }

    /**
     * 根据重试配置计算redis的ttl
     */
    private Duration calculateTimeout(DelayMsg<?> msg) {
        return msg.getDelay().plus(properties.getAllowedSendMaxDelay());
    }

    /**发送消息*/
    private void doSend(@NotBlank String appName, @NonNull DelayMsg<?> msg) {
        String redisKey = Supporter.getRedisKey(appName, msg);
        boolean exists = exists(redisKey);
        switch (msg.getConflictStrategy()) {
            // 重复抛异常，不重复写key发送消息
            case EXCEPTION: {
                sendMessage(queueName, serialize(msg), singletonMap(MSG_HEADER_X_DELAY, msg.getDelayMs()), () -> {
                    if (exists || !setRedisKey(redisKey, calculateTimeout(msg)))
                        throw new MsgConflictException(msg, appName);
                    else
                        return true;
                }, () -> removeRedisKey(redisKey));
                break;
            }
            // 不重复写key发送消息
            case IGNORE: {
                sendMessage(queueName, serialize(msg), singletonMap(MSG_HEADER_X_DELAY, msg.getDelayMs()), () -> !exists && setRedisKey(redisKey, calculateTimeout(msg)),
                        () -> removeRedisKey(redisKey));
            }
            break;
            // 直接发消息
            case REPEAT:
                sendMessage(queueName, serialize(msg), singletonMap(MSG_HEADER_X_DELAY, msg.getDelayMs()), null,null);
                break;
            // 重复，increase version，记录version到消息体后发送，刷新ttl;不重复，version=1，处理后发送
            case OVERWRITE:
                int version = 1;
                if (exists) {
                    version = requireNonNull(redisTemplate.opsForValue().increment(redisKey, 1)).intValue();
                    redisTemplate.expire(redisKey, calculateTimeout(msg));
                } else if (!setRedisKey(redisKey, calculateTimeout(msg)))
                    break;
                sendMessage(queueName, serializeWithVersion(msg, version), singletonMap(MSG_HEADER_X_DELAY, msg.getDelayMs()),null, null);
                break;
            default:
                break;
        }
    }

    /**
     * 根据msg写入redis占位符
     */
    private boolean setRedisKey(String redisKey, Duration timeout) {
        log.debug("DelayMsg key :{},ttl:{}", redisKey, timeout);
        return (TRUE.equals(redisTemplate.opsForValue().setIfAbsent(redisKey, 1, timeout)));
    }

    /**
     * 移除redis占位符
     */
    private boolean removeRedisKey(String redisKey) {
        return TRUE.equals(redisTemplate.delete(redisKey));
    }

    @Override
    public <T> boolean revokeFrom(@NonNull String appName, @NonNull DelayMsg<T> msg) {
        switch (msg.getConflictStrategy()) {
            case EXCEPTION:
            case OVERWRITE:
            case IGNORE:
                return removeRedisKey(Supporter.getRedisKey(appName, msg));
            case REPEAT:
                return true;
            default:
                throw new IllegalStateException("Unknown conflictStrategy" + msg.getConflictStrategy() + ": " + msg);
        }
    }

    @Override
    public boolean exists(String appName, @NonNull DelayMsg<?> msg) {
        String redisKey = Supporter.getRedisKey(appName, msg);
        switch (msg.getConflictStrategy()) {
            case EXCEPTION:
            case OVERWRITE:
            case IGNORE:
                return exists(redisKey);
            case REPEAT:
                log.warn("Repeat Strategy is not supported to get exists status");
                return false;
            default:
                throw new IllegalStateException("Unknown conflictStrategy" + msg.getConflictStrategy() + ": " + msg);
        }
    }

    private boolean exists(String redisKey) {
        return TRUE.equals(redisTemplate.hasKey(redisKey));
    }
}
