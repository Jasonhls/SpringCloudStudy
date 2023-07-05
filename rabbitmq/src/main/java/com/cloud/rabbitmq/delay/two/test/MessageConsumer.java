package com.cloud.rabbitmq.delay.two.test;

import cn.hutool.json.JSONUtil;
import com.cloud.mq.delayqueue.rabbitmq.one.Student;
import com.cloud.mq.delayqueue.rabbitmq.two.DelayMsgListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: 何立森
 * @Date: 2023/03/30/14:54
 * @Description:
 */
@Slf4j
@Component
public class MessageConsumer {

    @DelayMsgListener(value = "orderPayTest")
    public void onMessage(Student student) {
        log.info("消费的延迟消息为：{}", JSONUtil.toJsonStr(student));
    }
}
