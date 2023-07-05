package com.cloud.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 何立森
 * @Date: 2023/03/31/18:11
 * @Description:
 */
@Configuration
public class RabbitConfig {
    public static final String EXCHANGE_NAME = "RETRY_TEST_EXCHANGE";
    public static final String QUEUE_NAME = "RETRY_TEST_QUEUE";

    public static final String RETRY_QUEUE_KEY = "RETRY_TEST_QUEUE_KEY";

    @Bean
    public DirectExchange exchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public Binding bind() {
        return BindingBuilder.bind(queue()).to(exchange()).with(RETRY_QUEUE_KEY);
    }


}
