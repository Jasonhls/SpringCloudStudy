package com.cloud.mq.delayqueue.rabbitmq.two.exception;

import com.cloud.mq.delayqueue.rabbitmq.two.vo.DelayMsg;

import static com.cloud.mq.delayqueue.rabbitmq.two.support.Supporter.getRedisKey;

/**
 * 消息冲突异常
 */
public class MsgConflictException extends IllegalStateException {
    public MsgConflictException(DelayMsg<?> msg, String appName) {
        super("延迟消息[" + msg + "]已存在,redisKey:" + getRedisKey(appName, msg));
    }
}
