//package com.cloud.mq.consumer.rabbitmq;
//
//import cn.hutool.json.JSONUtil;
//import com.alibaba.fastjson.JSONObject;
//import com.cloud.mq.pojo.OrderSimpleInfo;
//import com.rabbitmq.client.Channel;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageProperties;
//import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
//import org.springframework.amqp.utils.SerializationUtils;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Service;
//import org.springframework.util.ClassUtils;
//
//import java.io.ByteArrayInputStream;
//import java.util.Arrays;
//import java.util.LinkedHashSet;
//import java.util.Set;
//
///**
// * @Author: 何立森
// * @Date: 2023/02/21/17:05
// * @Description:
// */
//@Service
//@Slf4j
//@Scope("prototype")
//public class HandleService implements ChannelAwareMessageListener {
//
//    private static final Set<String> whiteListPatterns = new LinkedHashSet(Arrays.asList("java.util.*", "java.lang.*"));
//
//
//    @Override
//    public void onMessage(Message message, Channel channel) throws Exception {
//        String s = handleMessage(message);
//        log.info("接受到的消息：{}", s);
//        try {
//            OrderSimpleInfo orderSimpleInfo = JSONObject.parseObject(s, OrderSimpleInfo.class);
//            log.info("消费成功:{}", JSONUtil.toJsonStr(orderSimpleInfo));
//            //确认消息消费成功
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        } catch (Exception e) {
//            //丢弃消息
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
//            log.error("消息消费失败", e);
//        }
//    }
//
//    public String handleMessage(Message message) {
//        try {
//            MessageProperties messageProperties = message.getMessageProperties();
//            boolean nullProps = messageProperties == null;
//            String contentType = nullProps ? null : messageProperties.getContentType();
//            if ("application/x-java-serialized-object".equals(contentType)) {
//                return SerializationUtils.deserialize(new ByteArrayInputStream(message.getBody()), whiteListPatterns, ClassUtils.getDefaultClassLoader()).toString();
//            }
//
//            String encoding = "utf-8";
//            if ("text/plain".equals(contentType) || "application/json".equals(contentType) || "text/x-json".equals(contentType) || "application/xml".equals(contentType)) {
//                return new String(message.getBody(), encoding);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        log.info("消息处理不了");
//        return null;
//    }
//}
