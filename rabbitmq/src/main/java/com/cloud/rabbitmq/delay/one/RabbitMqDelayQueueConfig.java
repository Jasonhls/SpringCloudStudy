package com.cloud.rabbitmq.delay.one;//package com.cloud.es.delay;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Irving
 * @date 2021/7/8 13:16
 */
//@Configuration
//@ConditionalOnProperty(value = "delay-queue.rabbitmq.enabled", havingValue = "true")
public class RabbitMqDelayQueueConfig {

    @Autowired
    RabbitMqDelayProperties rabbitMqDelayProperties;


    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<>(16);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(rabbitMqDelayProperties.getXdelayExchange(), "x-delayed-message", true, false, args);
    }

    @Bean
    public Queue queue() {
        return new Queue(rabbitMqDelayProperties.getXdelayQueue(), true);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(delayExchange()).with(rabbitMqDelayProperties.getXdelayQueue()).noargs();
    }


}
