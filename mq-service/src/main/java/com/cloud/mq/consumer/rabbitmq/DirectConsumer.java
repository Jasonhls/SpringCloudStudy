//package com.cloud.mq.consumer.rabbitmq;
//
//import cn.hutool.json.JSONUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Map;
//
///**
// * @Author: 何立森
// * @Date: 2023/02/21/10:07
// * @Description:
// */
//@Component
//@Slf4j
//@RabbitListener(queues = {"TestDirectQueue"})
//public class DirectConsumer {
//
//    @RabbitHandler
//    public void process(Map<String, Object> map) {
//        log.info("direct模式，消费消息时间为：{}，消息内容为：{}",
//                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
//                JSONUtil.toJsonStr(map));
//    }
//}
