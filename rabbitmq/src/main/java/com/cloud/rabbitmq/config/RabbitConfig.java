package com.cloud.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 何立森
 * @Date: 2023/03/31/18:11
 * @Description:
 */
@Configuration
public class RabbitConfig {
    public static final String EXCHANGE_NAME = "RETRY_TEST_EXCHANGE";
    public static final String DEAD_EXCHANGE_NAME = "DEAD_TEST_EXCHANGE";
    public static final String DEAD_EXCHANGE_TWO_NAME = "DEAD_TEST_TWO_EXCHANGE";


    public static final String AUTO_QUEUE_NAME = "RETRY_TEST_AUTO_QUEUE";
    public static final String MANUAL_QUEUE_NAME = "RETRY_TEST_MANUAL_QUEUE";
    public static final String NORMAL_QUEUE_NAME = "NORMAL_QUEUE_NAME";
    public static final String NORMAL_QUEUE_TWO_NAME = "NORMAL_QUEUE_TWO_NAME";
    public static final String DEAD_QUEUE_NAME = "DEAD_TEST_QUEUE";
    public static final String DEAD_QUEUE_TWO_NAME = "DEAD_TEST_TWO_QUEUE";

    public static final String RETRY_AUTO_QUEUE_KEY = "RETRY_TEST_AUTO_QUEUE_KEY";
    public static final String RETRY_MANUAL_QUEUE_KEY = "RETRY_TEST_MANUAL_QUEUE_KEY";
    public static final String NORMAL_QUEUE_KEY = "NORMAL_QUEUE_KEY";
    public static final String NORMAL_QUEUE_TWO_KEY = "NORMAL_QUEUE_TWO_KEY";
    public static final String DEAD_QUEUE_KEY = "DEAD_TEST_QUEUE_KEY";
    public static final String DEAD_QUEUE_TWO_KEY = "DEAD_TEST_QUEUE_TWO_KEY";


    /**
     * 正常交换机
     * @return
     */
    @Bean
    public DirectExchange exchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
    }

    /*-------------------------------自动确认模式正常队列-------------------------------------*/
    /**
     * 自动确认队列
     * @return
     */
    @Bean
    public Queue autoQueue() {
        return QueueBuilder.durable(AUTO_QUEUE_NAME).build();
    }

    /**
     * 自动确认绑定
     * @return
     */
    @Bean
    public Binding autoBind() {
        return BindingBuilder.bind(autoQueue()).to(exchange()).with(RETRY_AUTO_QUEUE_KEY);
    }

    /*-------------------------------手动确认模式正常队列-------------------------------------*/
    /**
     * 手动确认队列
     * @return
     */
    @Bean
    public Queue manualQueue() {
        return QueueBuilder.durable(MANUAL_QUEUE_NAME).build();
    }

    /**
     * 手动确认绑定
     * @return
     */
    @Bean
    public Binding manualBind() {
        return BindingBuilder.bind(manualQueue()).to(exchange()).with(RETRY_MANUAL_QUEUE_KEY);
    }

    /*-------------------------------------手动确认模式正常队列，配置死信队列------------------------------------*/
    /**
     * 自动确认队列
     * @return
     */
    @Bean
    public Queue normalQueue() {
        /**
         * 下面是正常队列绑定死信交换机的步骤
         */
        Map<String, Object> args = new HashMap<>(2);
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        args.put("x-dead-letter-routing-key", DEAD_QUEUE_KEY);
        return new Queue(NORMAL_QUEUE_NAME, true, false, false, args);
    }

    /**
     * 自动确认绑定
     * @return
     */
    @Bean
    public Binding normalBind() {
        return BindingBuilder.bind(normalQueue()).to(exchange()).with(NORMAL_QUEUE_KEY);
    }

    /*-------------------------------死信队列配置1-------------------------------------*/
    /**
     * 死信队列交换机
     * @return
     */
    @Bean
    public DirectExchange deadExchange() {
        return ExchangeBuilder.directExchange(DEAD_EXCHANGE_NAME).durable(true).build();
    }

    /**
     * 申明死信队列
     * @return
     */
    @Bean
    public Queue deadQueue() {
        return QueueBuilder.durable(DEAD_QUEUE_NAME).build();
    }

    /**
     * 将死信队列绑定到死信交换机
     * @return
     */
    @Bean
    public Binding deadBind() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(DEAD_QUEUE_KEY);
    }

    /*-------------------------------自动确认模式正常队列，配置死信队列-------------------------------------*/
    /**
     * 自动确认队列
     * @return
     */
    @Bean
    public Queue normalQueueTwo() {
        /**
         * 下面是正常队列绑定死信交换机的步骤
         */
        Map<String, Object> args = new HashMap<>(2);
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE_TWO_NAME);
        args.put("x-dead-letter-routing-key", DEAD_QUEUE_TWO_KEY);
        return new Queue(NORMAL_QUEUE_TWO_NAME, true, false, false, args);
    }

    /**
     * 自动确认绑定
     * @return
     */
    @Bean
    public Binding normalBindTwo() {
        return BindingBuilder.bind(normalQueueTwo()).to(exchange()).with(NORMAL_QUEUE_TWO_KEY);
    }


    /*-------------------------------死信队列配置2-------------------------------------*/
    /**
     * 死信队列交换机
     * @return
     */
    @Bean
    public DirectExchange deadExchangeTwo() {
        return ExchangeBuilder.directExchange(DEAD_EXCHANGE_TWO_NAME).durable(true).build();
    }

    /**
     * 申明死信队列
     * @return
     */
    @Bean
    public Queue deadQueueTwo() {
        return QueueBuilder.durable(DEAD_QUEUE_TWO_NAME).build();
    }

    /**
     * 将死信队列绑定到死信交换机
     * @return
     */
    @Bean
    public Binding deadBindTwo() {
        return BindingBuilder.bind(deadQueueTwo()).to(deadExchangeTwo()).with(DEAD_QUEUE_TWO_KEY);
    }

}
