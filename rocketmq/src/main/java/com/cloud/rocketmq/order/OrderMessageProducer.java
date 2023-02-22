package com.cloud.rocketmq.order;

import cn.hutool.json.JSONUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/02/17/15:14
 * @Description:
 */
public class OrderMessageProducer {
    public static void main(String[] args) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("order_producer_group");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        Long orderId = 10L;
        //自定义一个MessageQueueSelector
        MessageQueueSelector messageQueueSelector = new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                //用订单id选择发送的MessageQueue
                Long orderId = (Long) o;
                long index = orderId % list.size();
                //选择一个MessageQueue发送该订单的消息
                return list.get((int) index);
            }
        };
        String topicName = "TopicOrderTest";
        //新增订单消息
        Message newOrderMsg = new Message(topicName, "提交订单".getBytes(RemotingHelper.DEFAULT_CHARSET));
        Message paySuccessOrderMsg = new Message(topicName, "支付订单".getBytes(RemotingHelper.DEFAULT_CHARSET));
        Message finishOrderMsg = new Message(topicName, "订单已完成".getBytes(RemotingHelper.DEFAULT_CHARSET));

        //顺序发送消息
        SendResult sendResult1 = producer.send(newOrderMsg, messageQueueSelector, orderId);
        SendResult sendResult2 = producer.send(paySuccessOrderMsg, messageQueueSelector, orderId);
        SendResult sendResult3 = producer.send(finishOrderMsg, messageQueueSelector, orderId);
        System.out.println("发送结果为：" + JSONUtil.toJsonStr(sendResult1));
        System.out.println("发送结果为：" + JSONUtil.toJsonStr(sendResult2));
        System.out.println("发送结果为：" + JSONUtil.toJsonStr(sendResult3));
    }
}
