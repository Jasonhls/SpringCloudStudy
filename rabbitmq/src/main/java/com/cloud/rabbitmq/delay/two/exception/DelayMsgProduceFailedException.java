package com.cloud.rabbitmq.delay.two.exception;

/**
 * 延迟消息生产异常
 */
public class DelayMsgProduceFailedException extends RuntimeException {
    public DelayMsgProduceFailedException(String message) {
        super(message);
    }

    public DelayMsgProduceFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
