//package com.cloud.mq.consumer.rocketmq;
//
//import cn.hutool.core.date.DateTime;
//import cn.hutool.core.date.DateUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.spring.annotation.ConsumeMode;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.stereotype.Component;
//
///**
// * @Author: 何立森
// * @Date: 2023/02/20/14:24
// * @Description:
// *
// * MessageListenerOrderly默认实现类DefaultMessageListenerOrderly中，consumeMessage方法中，已经是标准的try catch了，
// * 然后报异常会返回ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
// *
// * 将mq-service项目执行package打包命令，然后生成的jar包，让同事在他虚拟机上执行java -jar xxx.jar，这样本地与同事虚拟机两天服务都是消费者，即有两台
// * 消费者机器，然后默认的messageQueue是4，然后就有两个MessageQueue对应本地消费者机器，另外两个MessageQueue就对应同事虚拟机的消费者机器，实现不同的MessageQueue
// * 在不同的消费者机器上消费，同一个MessageQueue肯定在同一台消费者机器上消费。
// */
//@Component
//@RocketMQMessageListener(consumerGroup = "order_consumer_group", topic = "TopicOrderTest", consumeMode = ConsumeMode.ORDERLY)
//@Slf4j
//public class MessageConsumer implements RocketMQListener<String> {
//
//    @Override
//    public void onMessage(String s) {
//        log.info("消费时间为：{}，消息为：{}", DateUtil.format(new DateTime(), "yyyy-MM-dd HH:mm:ss"), s);
//    }
//}
