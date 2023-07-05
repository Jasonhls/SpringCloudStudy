package com.cloud.mq.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.trueland.tcloud.scrm.common.delaymsgqueue.DelayMsgListener;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author: 何立森
 * @Date: 2023/04/23/18:13
 * @Description:
 */
@Component
@Slf4j
public class ConsumerTestForDelay {
    @DelayMsgListener(bizCode = "student")
    public void consumer(Student student) {
        log.info("消费到的消息为：{}", JSONUtil.toJsonStr(student));
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }
}
