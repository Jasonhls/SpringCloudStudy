package com.cloud.mq.delayqueue.rabbitmq.two;

import net.trueland.scrm.common.delayqueue.vo.DelayMsg;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 延迟消息监听器
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DelayMsgListener {
    /**
     * 监听主题 {@link DelayMsg#getTopic() 消息对象主题字段}
     */
    @AliasFor("topic")
    String value() default "";

    @AliasFor("value")
    String topic() default "";
}
