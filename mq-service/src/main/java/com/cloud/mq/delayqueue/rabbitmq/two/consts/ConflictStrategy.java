package com.cloud.mq.delayqueue.rabbitmq.two.consts;

/**
 * 消息冲突时的处理策略
 */
public enum ConflictStrategy {
    /**
     * 重复时报错
     */
    EXCEPTION,
    /**
     * 重复时覆盖
     */
    OVERWRITE,
    /**
     * 重复的消息会被重复消费
     */
    REPEAT,
    /**
     * 重复就跳过，不作处理
     */
    IGNORE;
}
