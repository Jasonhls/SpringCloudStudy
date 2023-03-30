package com.cloud.mq.delayqueue.rabbitmq.two.sender;

import com.cloud.mq.delayqueue.rabbitmq.two.DelayMsgSender;
import lombok.Lombok;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.AmqpTemplate;

import java.util.Map;
import java.util.function.BooleanSupplier;

import static com.cloud.mq.delayqueue.rabbitmq.two.config.DelayMsgProperties.DEFAULT_EXCHANGE;
import static com.cloud.mq.delayqueue.rabbitmq.two.consts.Consts.TRACE_ID;

@Slf4j
public abstract class AbstractDelayMsgSender implements DelayMsgSender {
    /**
     * 消息延迟时间，提供给x-delay插件使用
     */
    protected static final String MSG_HEADER_X_DELAY = "x-delay";
    /**
     * 消息延迟时间，因为x-delay内容不会保留，另外开一个header来记录
     */
    protected static final String MSG_HEADER_X_RETRY_DELAY = "x-retry-delay";
    protected static final String MSG_HEADER_RETRY_COUNT = "x-retry-count";

    abstract AmqpTemplate getAmqpTemplate();

    /**
     * 通过rabbit发送消息
     * @param routingKey 路由key
     * @param content 消息体
     * @param headerMap 消息头
     * @param preCheck 发送前的检查
     * @param onFailure 发送消息异常后，抛出异常前要做的事情
     */
    void sendMessage(String routingKey, String content, final Map<String, Object> headerMap, BooleanSupplier preCheck, Runnable onFailure) {
        if (null != preCheck && !preCheck.getAsBoolean()) {
            return;
        }
        try {
            log.debug("sendMessage to exchange:{} with routingKey:{},content:{}", DEFAULT_EXCHANGE, routingKey, content);
            getAmqpTemplate().convertAndSend(DEFAULT_EXCHANGE, routingKey, content, message -> {
                if (null != headerMap) {
                    message.getMessageProperties().getHeaders().putAll(headerMap);
                }
                message.getMessageProperties().getHeaders().put(TRACE_ID, MDC.get(TRACE_ID));
                return message;
            });
        } catch (Exception e) {
            if (null != onFailure) {
                onFailure.run();
            }
            log.error("error occurred when sending delayMsg:{},headerMap:{},routingKey:{}", content, headerMap, routingKey, e);
            throw Lombok.sneakyThrow(e);
        }
    }
}
