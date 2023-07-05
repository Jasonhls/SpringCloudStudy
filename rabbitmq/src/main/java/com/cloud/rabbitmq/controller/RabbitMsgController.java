package com.cloud.rabbitmq.controller;

import com.cloud.rabbitmq.config.RabbitConfig;
import com.cloud.rabbitmq.pojo.Student;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Author: 何立森
 * @Date: 2023/03/31/17:09
 * @Description:
 */
@RestController
@RequestMapping(value = "/rabbitMsg")
public class RabbitMsgController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @PostMapping(value = "/sendMsg")
    public void send(@RequestBody Student student) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.RETRY_QUEUE_KEY, student, new CorrelationData(UUID.randomUUID().toString()));
    }

}
