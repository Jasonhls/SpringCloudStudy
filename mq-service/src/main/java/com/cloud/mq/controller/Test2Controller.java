package com.cloud.mq.controller;

import net.trueland.tcloud.scrm.common.delaymsgqueue.DelayMsgSender;
import net.trueland.tcloud.scrm.common.delaymsgqueue.vo.DelayMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

/**
 * @Author: 何立森
 * @Date: 2023/04/23/18:10
 * @Description:
 */
@RestController
@RequestMapping(value = "/test")
public class Test2Controller {

    @Autowired
    private DelayMsgSender delayMsgSender;
    @GetMapping(value = "/test")
    public void test(Integer m) {
        Student s = new Student();
        s.setName("张三");
        s.setAge(32);
        DelayMsg<String> delayMsg = new DelayMsg<>();
        delayMsgSender.send("student", s, Duration.ofSeconds(m));
    }

    public static void main(String[] args) {
        Duration duration1 = Duration.ofDays(49);
        Duration duration2 = Duration.ofMillis((2^32 -1));
        int i = duration1.compareTo(duration2);
        System.out.println(i);

        double pow = Math.pow(2, 32);
        long l = Math.round(pow) - 1;
        System.out.println(l);
    }
}
