package com.cloud.rabbitmq.delay.two;

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
     * 监听主题
     */
    @AliasFor("topic")
    String value() default "";

    @AliasFor("value")
    String topic() default "";
}
