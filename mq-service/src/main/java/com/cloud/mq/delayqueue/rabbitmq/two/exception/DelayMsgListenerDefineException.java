package com.cloud.mq.delayqueue.rabbitmq.two.exception;

/**
 * 延迟消息监听器定义异常，启动检测
 */
public class DelayMsgListenerDefineException extends RuntimeException {
    public DelayMsgListenerDefineException(String message) {
        super(message);
    }
}
