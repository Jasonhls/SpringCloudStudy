package com.cloud.rocketmq.controller;

import com.cloud.rocketmq.callback.SendCallbackListener;
import com.cloud.rocketmq.pojo.OrderSimpleInfo;
import com.cloud.rocketmq.pojo.OrderStep;
import com.cloud.rocketmq.service.RocketMqService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/01/28/17:28
 * @Description:
 */
@RestController
@RequestMapping("rocketmq")
@Slf4j
public class RocketmqController {
    @Autowired
    private RocketMqService rocketMqService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @PostMapping("sendMessage")
    public void sendMessage(String topic, String msg) {
        rocketMqService.sendMessage(topic, msg);
    }

    @Value(value = "${rocketmq.producer.topic}:${rocketmq.producer.sync-tag}")
    private String syncTag;

    @Value(value = "${rocketmq.producer.topic}:${rocketmq.producer.async-tag}")
    private String asyncTag;

    @Value(value = "${rocketmq.producer.topic}:${rocketmq.producer.oneway-tag}")
    private String onewayTag;

    /**
     * 发送同步消息
     * @param msg
     * @return
     */
    @PostMapping("/pushSyncMessage")
    public SendResult pushSyncMessage(String msg) {
        SendResult sendResult = rocketMQTemplate.syncSend(syncTag, msg);
        return sendResult;
    }

    /**
     * 发送同步消息
     * @param msg
     * @return
     */
    @PostMapping("/pushAsyncMessage")
    public void pushAsyncMessage(String msg) {
        // 设置发送地和消息信息并发送异步消息
        rocketMQTemplate.asyncSend(asyncTag, msg, new SendCallbackListener());
    }

    /**
     * 发送单向消息
     * @param msg
     * @return
     */
    @PostMapping("/pushOnewayMessage")
    public void pushOnewayMessage(String msg) {
        // 设置发送地和消息信息并发送异步消息
        rocketMQTemplate.sendOneWay(onewayTag, msg);
    }

    /**
     * 发送延迟消息
     * @param id
     * @return
     */
    @PostMapping("pushDelayMessage")
    public SendResult pushDelayMessage(int id) {
        String messageStr = "延迟消息，订单编号是：" + id;
        org.springframework.messaging.Message<String> message = MessageBuilder.withPayload(messageStr)
                .setHeader(RocketMQHeaders.KEYS, id)
                .build();
        //设置超时和延时推送
        //超时时针对请求broker然后结果返回给product的耗时
        //第三个参数是延迟等级
        //选择RocketMq并不支持任意时间的延时，需要设置几个固定的延时等级，从1s到2h分别对应着等级1到18
        // private String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
        return rocketMQTemplate.syncSend(syncTag, message, 1000L, 3);
    }


    /**
     * 发送包含顺序的单向消息
     * @param id
     * @return
     */
    @PostMapping("/pushSequenceMessage")
    public void pushSequenceMessage(int id) {
//        for (int i = 0; i < 3; i++) {
            String msgId = id + "";
            List<OrderStep> orderSteps = OrderStep.buildOrderSteps(msgId);
            for (OrderStep orderStep : orderSteps) {
                //构建消息
                String messageStr = String.format("order id : %s, desc : %s", orderStep.getId(), orderStep.getDesc());
                org.springframework.messaging.Message<String> message = MessageBuilder.withPayload(messageStr)
                        .setHeader(RocketMQHeaders.KEYS, orderStep.getId())
                        .build();
                //设置顺序下发规则器
                rocketMQTemplate.setMessageQueueSelector(new MessageQueueSelector() {
                    /**
                     * 设置放入同一个队列的规则
                     * @param list 消息列表
                     * @param message 当前消息
                     * @param o 比较的关键信息
                     * @return 消息队列
                     */
                    @Override
                    public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                        //根据当前消息的id，使用固定算法获取需要下发的队列
                        //（使用当前id和消息队列个数进行取模获取需要下发的队列，id和队列数量一样时，选择的队列肯定一样）
                        log.info("当前数：{}，集合size：{}", Integer.parseInt(String.valueOf(o)), list.size());
                        int queueNum = Integer.parseInt(String.valueOf(o)) % list.size();
                        log.info(String.format("queueNum : %s, message : %s", queueNum, new String(message.getBody())));
                        return list.get(queueNum);
                    }
                });
                //设置发送地和消息信息并发送消息（Orderly）
                rocketMQTemplate.syncSendOrderly(syncTag, message, orderStep.getId());
            }
//        }
    }

    /**
     * 发送sql过滤消息
     * @param id
     */
    @PostMapping("pushSqlMessage")
    public void pushSqlMessage(int id) {
        List<org.springframework.messaging.Message> messages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String myId = id + i + "";
            String messageStr = "order id : " + myId;
            org.springframework.messaging.Message<String> message = MessageBuilder.withPayload(messageStr)
                    .setHeader(RocketMQHeaders.KEYS, myId)
                    //设置tags: money > 5 可以过滤消息进行消费
                    .setHeader("money", i)
                    .build();
            messages.add(message);
        }
        rocketMQTemplate.syncSend(syncTag, messages);
    }

    /**
     * 发送顺序消息，根据订单id对messageQueue的个数取余，然后对不同id的消息，分到不同的messageQueue，然后messageQueue又对应某一个消费者机器，
     * 一个消费者机器可以对应多个messageQueue，这样同一个orderId，即同订单的消息，就会发送到同一个MessageQueue中，实现顺序消息消费。
     * @param info
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/sendOrderMessage")
    public List<SendResult> sendOrderMessage(@RequestBody OrderSimpleInfo info) throws Exception{
        String orderId = info.getOrderId();
        //自定义一个MessageQueueSelector
        MessageQueueSelector messageQueueSelector = new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                //用订单id选择发送的MessageQueue
                String str = (String)o;
                Long orderId = Long.valueOf(str);
                long index = orderId % list.size();
                log.info("总的messageQueue数量为：{}，当前选择的messageQueue为：{}", list.size(), index);
                //选择一个MessageQueue发送该订单的消息
                return list.get((int) index);
            }
        };
        rocketMQTemplate.setMessageQueueSelector(messageQueueSelector);

        String topicName = "TopicOrderTest";
        //顺序发送消息
        //设置发送地和消息信息并发送消息（Orderly）
        SendResult sendResult1 = rocketMQTemplate.syncSendOrderly(topicName, "创建订单", orderId);
        SendResult sendResult2 = rocketMQTemplate.syncSendOrderly(topicName, "支付订单", orderId);
        SendResult sendResult3 = rocketMQTemplate.syncSendOrderly(topicName, "订单已完成", orderId);

        List<SendResult> result = new ArrayList<>();
        result.add(sendResult1);
        result.add(sendResult2);
        result.add(sendResult3);
        return result;
    }


}
