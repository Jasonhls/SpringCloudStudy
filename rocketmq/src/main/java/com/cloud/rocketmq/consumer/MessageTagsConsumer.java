package com.cloud.rocketmq.consumer;

import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @Author: 何立森
 * @Date: 2023/01/29/10:45
 * @Description:
 * 设置消息监听
 * 监听组：监听topic：监听tag（默认监听topic下所有）
 * 监听消费模式：默认负载均衡：CLUSTERING（每一个消息只发给一个消费者）、广播模式：BROADCASTING(发送给所有消费者)
 */
@Component
@RocketMQMessageListener(consumerGroup = "${rocketmq.consumer.group}", topic = "${rocketmq.consumer.topic}",
    selectorExpression = "${rocketmq.consumer.tags}", messageModel = MessageModel.BROADCASTING
)
public class MessageTagsConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("test服务消费消息:" + message);
    }
}
