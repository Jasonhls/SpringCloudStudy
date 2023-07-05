package com.cloud.rabbitmq.delay.two.test;

import com.cloud.mq.delayqueue.rabbitmq.one.Student;
import com.cloud.mq.delayqueue.rabbitmq.two.sender.AbstractDelayMsgSender;
import com.cloud.mq.delayqueue.rabbitmq.two.vo.DelayMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;

/**
 * @Author: 何立森
 * @Date: 2023/03/30/14:30
 * @Description:
 */
//@RestController
//@RequestMapping(value = "/message")
//@Slf4j
public class MessageController {
    @Autowired
    private AbstractDelayMsgSender delayMsgSender;

    @PostMapping(value = "/sendMessage")
    public void sendMessage(@RequestBody Student student) {
        DelayMsg<Student> msg = new DelayMsg<>("orderPayTest", student, Duration.ofSeconds(5));
        delayMsgSender.send(msg);
    }

}
