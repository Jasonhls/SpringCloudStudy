package com.cloud.rocketmq.order;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/02/17/15:31
 * @Description:
 */
public class OrderMessageConsumer {
    public static void main(String[] args) throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("order_consumer_group");
        consumer.setNamesrvAddr("localhost:9876");
        String topicName = "TopicOrderTest";
        consumer.subscribe(topicName, "*");
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                consumeOrderlyContext.setAutoCommit(true);
                try {
                    for (MessageExt msg : list) {
                        //对有序的消息进行处理
                        String str = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);
                        System.out.println("消费时间为：" + DateUtil.format(new DateTime(), "yyyy-MM-dd HH:mm:ss") + "，消息为：" + str);
                    }
                    DateTime date = DateUtil.parse("2023-02-17 17:09:00");
                    if(date.compareTo(new DateTime()) > 0) {
                        throw new RuntimeException("运行异常");
                    }
                    return ConsumeOrderlyStatus.SUCCESS;
                }catch (Exception e) {
                    //如果消息处理有问题
                    //返回一个状态，让他暂停一会儿再继续处理这批消息(暂停一秒后就重试)
                    System.out.println("抛出异常啦");
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            }
        });


        consumer.start();
    }
}
