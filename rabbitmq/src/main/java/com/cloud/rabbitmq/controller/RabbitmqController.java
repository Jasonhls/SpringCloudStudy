package com.cloud.rabbitmq.controller;

import cn.hutool.json.JSONUtil;
import com.cloud.rabbitmq.config.RabbitConfig2;
import com.cloud.rabbitmq.pojo.OrderSimpleInfo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: 何立森
 * @Date: 2023/02/21/9:05
 * @Description:
 */
@RestController
@RequestMapping(value = "/rabbitmq")
public class RabbitmqController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping(value = "/sendDirectMessage")
    public String sendDirectMessage(@RequestBody OrderSimpleInfo info) {
        String messageId = String.valueOf(UUID.randomUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>(4);
        map.put("messageId", messageId);
        map.put("orderId", info.getOrderId());
        map.put("desc", info.getDesc());
        map.put("createTime", createTime);
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
        return "ok";
    }

    /**
     * 一共发送四条消息，如果有两条消费者机器，都监听同一个queue，那么会采用轮循的方式消费，即本机器消费者消费第一条，第三条消息，而
     * 同事虚拟机上的消费者机器消费第二条，第四条消息。
     * @param list
     * @return
     */
    @PostMapping(value = "/sendDirectBatchMessage")
    public String sendDirectBatchMessage(@RequestBody List<OrderSimpleInfo> list) {
        String messageId = String.valueOf(UUID.randomUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        for (int i = 0; i < list.size(); i++) {
            OrderSimpleInfo info = list.get(i);
            Map<String, Object> map = new HashMap<>(4);
            map.put("messageId", messageId);
            map.put("orderId", info.getOrderId());
            map.put("desc", info.getDesc());
            map.put("createTime", createTime);
            rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
        }
        return "ok";
    }

    @Autowired
    private RabbitConfig2 rabbitConfig;

    @PostMapping(value = "/sendTopicBatchMessage")
    public String sendTopicBatchMessage(@RequestBody List<OrderSimpleInfo> list) throws Exception{
        String messageId = String.valueOf(UUID.randomUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        for (int i = 0; i < list.size(); i++) {
            OrderSimpleInfo info = list.get(i);
            Map<String, Object> map = new HashMap<>(4);
            map.put("messageId", messageId);
            map.put("orderId", info.getOrderId());
            map.put("createTime", createTime);



            String routingKey = rabbitConfig.getRoutingKey(info.getOrderId());

            map.put("desc", info.getName() + "进入了直播间。");
            rabbitTemplate.convertAndSend("OrderlyTopicExchange", routingKey, map);

            map.put("desc", info.getName() + "浏览了商品。");
            rabbitTemplate.convertAndSend("OrderlyTopicExchange", routingKey, map);

            map.put("desc", info.getName() + "创建了订单。");
            rabbitTemplate.convertAndSend("OrderlyTopicExchange", routingKey, map);

            map.put("desc", info.getName() + "支付了订单。");
            rabbitTemplate.convertAndSend("OrderlyTopicExchange", routingKey, map);
        }
        return "ok";
    }

    @PostMapping(value = "/sendTopicMessageOrderly")
    public String sendTopicMessageOrderly(@RequestBody OrderSimpleInfo info) {
        String routingKey = rabbitConfig.getRoutingKey(info.getOrderId());
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        info.setDesc("同步会员信息");
        Message message = new Message(JSONUtil.toJsonStr(info).getBytes(StandardCharsets.UTF_8), messageProperties);
        rabbitTemplate.convertAndSend("OrderlyTopicExchange", routingKey, message);
        info.setDesc("同步会员积分信息");
        Message message2 = new Message(JSONUtil.toJsonStr(info).getBytes(StandardCharsets.UTF_8), messageProperties);
        rabbitTemplate.convertAndSend("OrderlyTopicExchange", routingKey, message2);
        info.setDesc("同步会员积分记录信息");
        Message message3 = new Message(JSONUtil.toJsonStr(info).getBytes(StandardCharsets.UTF_8), messageProperties);
        rabbitTemplate.convertAndSend("OrderlyTopicExchange", routingKey, message3);
        return "success";
    }

}
