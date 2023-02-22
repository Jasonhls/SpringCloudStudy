package com.cloud.rocketmq.transaction;

import cn.hutool.json.JSONUtil;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.*;

/**
 * @Author: 何立森
 * @Date: 2023/02/16/15:23
 * @Description:
 */
public class TransactionProducer {
    public static void main(String[] args) throws Exception{
        //创建用来接口RocketMQ回调的一个监听器接口
        //这里会实现执行订单本地事务，commit,rollback，回调查询等逻辑
        TransactionListener transactionListener = new TransactionListenerImpl();
        //创建支持事务消息的Producer，需要指定一个生产者分组，随便指定一个名字
        TransactionMQProducer producer = new TransactionMQProducer("TestProducerGroup");
        producer.setNamesrvAddr("localhost:9876");
        ExecutorService executorService = new ThreadPoolExecutor(
                2,
                5,
                100,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("TestThread");
                        return thread;
                    }
                }
        );
        // 给事务消息生产者设置对应的线程池，负责执行RocketMQ回调请求
        producer.setExecutorService(executorService);
        //给事务消息生产者设置对应的回调函数
        producer.setTransactionListener(transactionListener);
        //启动这个事务消息生产者
        producer.start();

        //构造一条订单支付成功的消息，指定Topic是谁
        Message msg = new Message(
                "PayOrderSuccessTopic",
                "TestTag",
                "TestKey",
                "订单支付消息".getBytes(RemotingHelper.DEFAULT_CHARSET));
        //将消息作为half消息的模式发送出去
        TransactionSendResult sendResult = producer.sendMessageInTransaction(msg, null);
        System.out.println(JSONUtil.toJsonStr(sendResult));
    }
}
