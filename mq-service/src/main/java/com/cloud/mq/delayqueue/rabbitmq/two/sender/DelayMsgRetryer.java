package com.cloud.mq.delayqueue.rabbitmq.two.sender;

import com.cloud.mq.delayqueue.rabbitmq.two.vo.DelayMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;

import javax.validation.constraints.NotBlank;
import java.util.function.Consumer;

import static com.cloud.mq.delayqueue.rabbitmq.two.config.DelayMsgProperties.DEFAULT_EXCHANGE;
import static com.cloud.mq.delayqueue.rabbitmq.two.sender.AbstractDelayMsgSender.*;

/**
 * 延迟消息重试
 */
@RequiredArgsConstructor
@Slf4j
public class DelayMsgRetryer {
    private final RabbitProperties rabbitProperties;
    private final AbstractDelayMsgSender delayMsgSender;
    private final String queueName;
    private final Consumer<DelayMsg<?>> cleanAfterFailed;
    private final MessageRecoverer messageRecoverer;

    /**
     * 检查是否可重试
     */
    public boolean check(DelayMsg<?> msg, Exception e) {
        return true;
    }

    /**
     * 重试
     * @param message 原始消息
     */
    public void retry(@NotBlank Message message, DelayMsg<?> msg, Exception e) {
        RabbitProperties.ListenerRetry retry = rabbitProperties.getListener().getSimple().getRetry();
        if (!retry.isEnabled()) {
            log.trace("DelayMsg retry disabled");
            doAfterFailed(message, msg, e);
            return;
        }
        if (!checkMaxAttempts(message, retry)) {
            doAfterFailed(message, msg, e);
            return;
        }
        if (!checkMaxInterval(message, retry)) {
            doAfterFailed(message, msg, e);
            return;
        }
        if (!check(msg, e)) {
            doAfterFailed(message, msg, e);
        } else {
            log.info("Retry sending delayMsg at retries:{},delay:{},msg:{} ", message.getMessageProperties().getHeader(MSG_HEADER_RETRY_COUNT),
                    message.getMessageProperties().getHeader(MSG_HEADER_X_RETRY_DELAY), msg);
            delayMsgSender.getAmqpTemplate().send(DEFAULT_EXCHANGE, queueName, message);
        }
    }

    /**
     * 处理重试次数
     */
    private boolean checkMaxAttempts(Message message, RabbitProperties.ListenerRetry retry) {
        int retryCount = 0;
        Number num = message.getMessageProperties().getHeader(MSG_HEADER_RETRY_COUNT);
        if (null != num) {
            retryCount = num.intValue();
        }
        retryCount++;
        if (retryCount > retry.getMaxAttempts()) {
            log.warn("Retry delayMsg failed due to attempts reach max [" + retry.getMaxAttempts() + "]");
            return false;
        } else {
            message.getMessageProperties().getHeaders().put(MSG_HEADER_RETRY_COUNT, retryCount);
            return true;
        }
    }

    /**
     * 处理重试间隔
     */
    private boolean checkMaxInterval(Message message, RabbitProperties.ListenerRetry retry) {
        int retryCount = message.getMessageProperties().getHeader(MSG_HEADER_RETRY_COUNT);
        long delay;
        if (retryCount == 1) {
            delay = retry.getInitialInterval().toMillis();
        } else {
            Number preDelay = message.getMessageProperties().getHeader(MSG_HEADER_X_RETRY_DELAY);
            if (null == preDelay) {
                log.error("Retry delayMsg failed , preDelay should not be null");
                return false;
            } else
                delay = (long) (preDelay.longValue() * retry.getMultiplier());
        }
        if (delay >= retry.getMaxInterval().toMillis()) {
            log.warn("Retry delayMsg failed due to interval reach max [" + retry.getMaxInterval() + "]");
            return false;
        } else {
            message.getMessageProperties().getHeaders().put(MSG_HEADER_X_DELAY, delay);
            message.getMessageProperties().getHeaders().put(MSG_HEADER_X_RETRY_DELAY, delay);
            return true;
        }
    }

    /**
     * 重试失败（完结）后的处理
     */
    private void doAfterFailed(Message message, DelayMsg<?> msg, Exception e) {
        if (null != cleanAfterFailed)
            cleanAfterFailed.accept(msg);
        if (null != messageRecoverer)
            messageRecoverer.recover(message, e);
    }
}
