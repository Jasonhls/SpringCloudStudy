package com.cloud.rabbitmq.controller;

import cn.hutool.json.JSONUtil;
import com.cloud.rabbitmq.config.RabbitConfig;
import com.cloud.rabbitmq.pojo.Student;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
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
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping(value = "/sendAutoMsg")
    public void sendAutoMsg(@RequestBody Student student) {
        //直接发送不能转换成student对象，需要自己定义编码
        Message message = new Message(JSONUtil.toJsonStr(student).getBytes(StandardCharsets.UTF_8), new MessageProperties());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.RETRY_AUTO_QUEUE_KEY, message, new CorrelationData(UUID.randomUUID().toString()));
    }

    @PostMapping(value = "/sendManualMsg")
    public void sendManualMsg(@RequestBody Student student) {
        //直接发送不能转换成student对象，需要自己定义编码
        Message message = new Message(JSONUtil.toJsonStr(student).getBytes(StandardCharsets.UTF_8), new MessageProperties());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.RETRY_MANUAL_QUEUE_KEY, message, new CorrelationData(UUID.randomUUID().toString()));
    }

    @PostMapping(value = "/sendNormalMsg")
    public void sendNormalMsg(@RequestBody Student student) {
        //直接发送不能转换成student对象，需要自己定义编码
        Message message = new Message(JSONUtil.toJsonStr(student).getBytes(StandardCharsets.UTF_8), new MessageProperties());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.NORMAL_QUEUE_KEY, message, new CorrelationData(UUID.randomUUID().toString()));
    }

    @PostMapping(value = "/sendNormalTwoMsg")
    public void sendNormalTwoMsg(@RequestBody Student student) {
        //直接发送不能转换成student对象，需要自己定义编码
        Message message = new Message(JSONUtil.toJsonStr(student).getBytes(StandardCharsets.UTF_8), new MessageProperties());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.NORMAL_QUEUE_TWO_KEY, message, new CorrelationData(UUID.randomUUID().toString()));
    }

    @PostMapping(value = "/getCacheData")
    public Student getCacheData(@RequestParam("key") String key) {
        if(!redisTemplate.hasKey(key)) {
            return null;
        }
        String value = (String)redisTemplate.opsForValue().get(key);
        return JSONUtil.toBean(value, Student.class);
    }

}
