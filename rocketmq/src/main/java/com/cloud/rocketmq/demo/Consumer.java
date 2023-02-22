package com.cloud.rocketmq.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/02/01/17:13
 * @Description:
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_consumer");

        //设置NameServer的地址
        consumer.setNamesrvAddr("localhost:9876");
        //订阅Topic，你要消费哪些Topic的消息
        consumer.subscribe("TopicTest", "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                    ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                try {
                    //在这里对获取到的list订单消息进行处理
                    //比如增加积分、发送优惠券、通知发货等等
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }catch (Exception e) {
                    //如果因为数据库宕机等问题，对消息处理失败了
                    //此时返回一个稍后重试消费的状态
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });
        consumer.start();
    }
}
