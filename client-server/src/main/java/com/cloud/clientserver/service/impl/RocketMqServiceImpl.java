package com.cloud.clientserver.service.impl;

import com.cloud.clientserver.service.RocketMqService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 何立森
 * @Date: 2023/01/28/17:24
 * @Description:
 */
@Service
public class RocketMqServiceImpl implements RocketMqService {


    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void sendMessage(String topic, String msg) {
        rocketMQTemplate.convertAndSend(topic, msg);
    }

}
