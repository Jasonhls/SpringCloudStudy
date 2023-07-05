package com.cloud.rabbitmq.consumer;

import cn.hutool.json.JSONUtil;
import com.cloud.rabbitmq.config.RabbitConfig;
import com.cloud.rabbitmq.pojo.Student;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: 何立森
 * @Date: 2023/03/31/18:26
 * @Description:
 */
@Component
@Slf4j
public class RetryConsumeMsg {
    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void consume(Message message, Channel channel) {
        byte[] body = message.getBody();
        String msg = new String(body);
        Student student = JSONUtil.toBean(msg, Student.class);
        if(student.getAge() == 31) {
            throw new RuntimeException("【重试测试】抛出错误！");
        }
        log.info("【重试测试】消费消息为：{}", JSONUtil.toJsonStr(student));
    }
}
