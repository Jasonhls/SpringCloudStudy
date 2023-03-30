package com.cloud.mq.delayqueue.rabbitmq.two.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author Irving
 * @date 2021/7/8 13:27
 */
@Data
@ConfigurationProperties(prefix = "delay-msg")
public class DelayMsgProperties {

    public static final String DEFAULT_EXCHANGE = "default.delay.exchange";
    /**
     * 启用延迟队列
     */
    private boolean enabled = true;
    /**
     * 并发消费数量
     */
    private int consumeConcurrency = 2;
    /**
     * 最大并发消费数量
     */
    private int maxConsumeConcurrency = 6;
    /**
     * 允许发送的最大延迟时间
     */
    private Duration allowedSendMaxDelay = Duration.ofDays(1);

}
