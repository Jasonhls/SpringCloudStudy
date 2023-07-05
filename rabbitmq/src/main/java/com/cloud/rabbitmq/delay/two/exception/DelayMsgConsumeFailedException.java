package com.cloud.rabbitmq.delay.two.exception;

/**
 * 延迟消息消费异常
 */
public class DelayMsgConsumeFailedException extends RuntimeException {
    public DelayMsgConsumeFailedException(String message) {
        super(message);
    }

    public DelayMsgConsumeFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
