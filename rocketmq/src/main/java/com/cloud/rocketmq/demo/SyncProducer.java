package com.cloud.rocketmq.demo;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;

/**
 * @Author: 何立森
 * @Date: 2023/02/01/17:07
 * @Description:
 */
public class SyncProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("test_producer");
        //这里需要设置NameServer地址
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        //死循环不停的发送消息，这里可以设置为启动多个线程
        //然后使用Producer去不停的发送消息
        for (int i = 0; i < 10; i++) {
            String s = i + 1 + "";
            new Thread(() -> {
//                while(true) {
                    try {
                        Message msg = new Message(
                                "TopicTest",
                                "TagA",
                                ("hello你好" + s).getBytes(RemotingHelper.DEFAULT_CHARSET)
                        );
                    SendResult send = producer.send(msg);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
//                }
            }).start();
        }
        //程序卡在这里，不能让它结束，就不停的发送消息
        /*while(true) {
            Thread.sleep(1000);
        }*/

    }
}
