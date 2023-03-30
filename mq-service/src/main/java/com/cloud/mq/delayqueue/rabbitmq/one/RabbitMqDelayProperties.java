package com.cloud.mq.delayqueue.rabbitmq.one;//package com.cloud.es.delay;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static cn.hutool.core.util.ObjectUtil.defaultIfBlank;

/**
 * @author Irving
 * @date 2021/7/8 13:21
 */
@Data
@Component
@ConfigurationProperties(prefix = "delay-queue.rabbitmq")
public class RabbitMqDelayProperties {

    @Value("${spring.application.name}")
    private String appName;
    @Value("${spring.profiles.active:default}")
    private String profile;


    /**
     * 队列名称
     */
    private String xdelayQueue;
    /**
     * 交换机名称
     */
    private String xdelayExchange = "default.delay.exchange";

    public String getXdelayQueue() {
        return defaultIfBlank(xdelayQueue, Objects.requireNonNull(appName) + "-" + profile);
    }
}
