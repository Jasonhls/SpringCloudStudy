package com.cloud.rocketmq.service;

/**
 * @Author: 何立森
 * @Date: 2023/01/28/17:24
 * @Description:
 */
public interface RocketMqService {
    void sendMessage(String topic, String msg);
}
