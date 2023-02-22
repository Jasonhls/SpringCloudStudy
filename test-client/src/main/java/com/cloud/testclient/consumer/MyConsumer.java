package com.cloud.testclient.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @Author: 何立森
 * @Date: 2023/01/28/17:36
 * @Description:
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "first-topic", consumerGroup = "springCloudGroup")
public class MyConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }
}
