//package com.cloud.mq.config;
//
//import com.cloud.mq.consumer.rabbitmq.HandleService;
//import com.cloud.mq.util.IpUtils;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.Connection;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @Author: 何立森
// * @Date: 2023/02/21/13:58
// * @Description:
// */
//@Configuration
//@Slf4j
//@Data
//public class RabbitmqConfig {
//
//    @Value("${spring.rabbitmq.host}")
//    private String host;
//
//    @Value("${spring.rabbitmq.port}")
//    private Integer port;
//
//    @Value("${spring.rabbitmq.username}")
//    private String username;
//
//    @Value("${spring.rabbitmq.password}")
//    private String password;
//
//    public static final Integer queueNum = 5;
//    public static final Integer concurrentConsumersNum = 2;
//
//    public static final Integer fetchCount = 100;
//
//    public static String exchangeName = "OrderlyTopicExchange";
//
//    /**
//     * 创建mq连接
//     * @return
//     */
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        //host port必须配置，不然到了linux上部署这个jar包，就会包连接拒绝
//        connectionFactory.setHost(host);
//        connectionFactory.setPort(port);
//        connectionFactory.setUsername(username);
//        connectionFactory.setPassword(password);
//        connectionFactory.setPublisherConfirms(true);
//        return connectionFactory;
//    }
//
//    @Bean
//    public RabbitAdmin rabbitAdmin() {
//        return new RabbitAdmin(connectionFactory());
//    }
//
//    /**
//     * 动态创建queue，命名为：hostName.queue1【192.168.1.1.queue1】,并返回数组queue名称
//     * @return
//     * @throws Exception
//     */
//    @Bean
//    public String[] mqMsgQueues(RabbitAdmin rabbitAdmin) throws Exception{
//        String[] queueNames = new String[queueNum];
//        String hostName = IpUtils.getIp();
//        TopicExchange topicExchange = new TopicExchange(exchangeName);
//        for (int i = 1; i <= queueNum; i++) {
//            String queueName = String.format("%s.queue%d", hostName, i);
//            Queue queue = new Queue(queueName, true);
//            rabbitAdmin.declareQueue(queue);
//            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(queueName));
////            connectionFactory().createConnection().createChannel(false).queueDeclare(queueName, true, false, false, null);
////            connectionFactory().createConnection().createChannel(false).queueBind(queueName, exchangeName, queueName);
//            queueNames[i - 1] = queueName;
//        }
//        return queueNames;
//    }
//
//
//    /**
//     * 创建监听器，监听队列
//     * @param handleService
//     * @return
//     * @throws Exception
//     */
//    @Bean
//    public SimpleMessageListenerContainer mqMessageContainer(HandleService handleService, RabbitAdmin rabbitAdmin) throws Exception{
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
//        container.setQueueNames(mqMsgQueues(rabbitAdmin));
//        container.setExposeListenerChannel(true);
//        //设置每个消费者获取的最大的消费数量
//        container.setPrefetchCount(fetchCount);
//        //消费者个数
//        container.setConcurrentConsumers(concurrentConsumersNum);
//        //设置确认模式为手工确认
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        //监听处理类
//        container.setMessageListener(handleService);
//        return container;
//    }
//}
