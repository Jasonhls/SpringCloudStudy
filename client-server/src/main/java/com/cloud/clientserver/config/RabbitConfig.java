package com.cloud.clientserver.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.cloud.clientserver.pojo.RabbitmqConsumerInfo;
import com.cloud.clientserver.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: 何立森
 * @Date: 2023/02/21/11:09
 * @Description:
 */
@Configuration
@Slf4j
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private String port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    public static final Integer queueNum = 5;

    public static String exchangeName = "OrderlyTopicExchange";

    /**
     * 创建mq连接
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    /**
     * 动态创建queue，命名为：hostName.queue1【192.168.1.1.queue1】,并返回数组queue名称
     * @return
     * @throws Exception
     * 192.168.1.1.queue1
     * 192.168.1.1.queue2
     * 192.168.1.1.queue3
     * 192.168.1.1.queue4
     * 192.168.1.1.queue5
     */
    @Bean
    public String[] mqMsgQueues() throws Exception{
        String[] queueNames = new String[queueNum];
        String hostName = IpUtils.getIp();
        for (int i = 1; i <= queueNum; i++) {
            String queueName = String.format("%s.queue%d", hostName, i);
            connectionFactory().createConnection().createChannel(false).queueDeclare(queueName, true, false, false, null);
            //第三个参数是routingKey
            connectionFactory().createConnection().createChannel(false).queueBind(queueName, exchangeName, queueName);
            queueNames[i - 1] = queueName;
        }
        return queueNames;
    }

    /**
     * 获取rabbitmq上所有机器上的该业务的所有的queue，然后取模选择一个queue，投递同订单的业务数据，然后消费者机器上，
     * 只消费包含该机器ip的queue，这样就能保证不同的机器消费不同的queue
     * @param orderId
     * @return
     */
    public String getRoutingKey(String orderId) {
        String body = HttpRequest.get("http://" +host +":15672/api/consumers")
                .basicAuth("admin", "admin")
                .execute().body();
        List<RabbitmqConsumerInfo> rabbitmqConsumerInfos = JSONArray.parseArray(body, RabbitmqConsumerInfo.class);
        Set<String> queuesName = new HashSet<>();
        for (RabbitmqConsumerInfo info : rabbitmqConsumerInfos) {
            String name = info.getQueue().getName();
            if(name.lastIndexOf("queue") == name.length() - 6) {
                queuesName.add(name);
            }
        }
        ArrayList<String> list = CollectionUtil.newArrayList(queuesName);
        log.info("全部的queue为：{}，总数为：{}", JSONUtil.toJsonStr(list), list.size());
        int index = Integer.valueOf(orderId) % list.size();
        log.info("路由index为：{}", index);
        return list.get(index);
    }
}
