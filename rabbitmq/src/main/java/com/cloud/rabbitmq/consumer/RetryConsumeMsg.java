package com.cloud.rabbitmq.consumer;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.cloud.rabbitmq.config.RabbitConfig;
import com.cloud.rabbitmq.pojo.Student;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: 何立森
 * @Date: 2023/03/31/18:26
 * @Description:
 */
@Component
@Slf4j
public class RetryConsumeMsg {

    @Autowired
    private RedisTemplate redisTemplate;

    /*---------------------------------自动模式下的正常队列，没有配置死信队列--------------------------------------------*/

    /**
     * ackMode: MANUAL 和 AUTO
     * @param message
     * @param channel
     * 自动确认模式：如果配置的spring.rabbitmq.listener.simple.retry.max-attempts = n，则第一次消费失败后，还会重试n - 1次，
     * 中间间隔时间是可以配置的
     */
    @RabbitListener(queues = RabbitConfig.AUTO_QUEUE_NAME, ackMode = "AUTO")
    public void consumeAutoMsg(Message message, Channel channel) {
        byte[] body = message.getBody();
        String msg = new String(body);
        log.info("【自动确认消费】消息内容为：{}", msg);
        Student student = JSON.parseObject(msg, Student.class);
        log.info("【自动确认消费】学生信息为：{}", JSONUtil.toJsonStr(student));
        if(student.getAge() == 31) {
            throw new RuntimeException("【重试测试】【自动确认消费】抛出错误！");
        }
    }

    /*---------------------------------手动模式下的正常队列，没有配置死信队列--------------------------------------------*/
    @RabbitListener(queues = RabbitConfig.MANUAL_QUEUE_NAME, ackMode = "MANUAL")
    public void consumeManualMsg(Message message, Channel channel) throws IOException {
        Student student = null;
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("【手动确认消费】消息内容为：{}", msg);
            student = JSON.parseObject(msg, Student.class);
            log.info("【手动确认消费】学生信息为：{}", JSONUtil.toJsonStr(student));
            if(student.getAge() == 31) {
                throw new RuntimeException("【重试测试】【手动确认消费】抛出错误！");
            }
            /**
             * deliveryTag：确认消息的编号，这是每个消息被消费时都会分配一个递增唯一编号
             * multiple：批量确认，true表示所有编号小于目前确认消息编号的待确认消息都会被确认，false则只确认当前消息
             * 特别注意：消息的编码是每个信道Channel范围的，批量确认操作也是针对当前Channel信道的操作。请务必记住这个范围
             */
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e) {
            e.printStackTrace();
            /**
             * basicNack:
             * void basicNack(long deliveryTag, boolean multiple, boolean requeue) throws IOException;
             * 删除、放回队列，通过参数requeue控制(true返回队列，false删除，删除是直接删除，也不会重试)，
             * 拒绝确认的消息放回队列时会放置在队列首位，拒绝消息不放回队列可以搭配死信队列使用，
             * 这个API相对于basicReject()而言多了一个参数multiple，效果与批量确认一致。
             * 注意：这里要慎用true，如果消费失败，返回队列，会造成死循环。
             * basicAck:
             * 直接确认，第二个参数multiple，是批量确认，true表示所有编号小于目前确认消息编号的待确认消息都会被确认，false则只确认当前消息
             * 特别注意：消息的编码是每个信道Channel范围的，批量确认操作也是针对当前Channel信道的操作。请务必记住这个范围
             */
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            /**
             * 直接ack确认了，消息会丢失，都不会重试，如果basicAck方法的第二参数multiple的值为true，也会直接丢失，不会重试
             */
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            //如果后续再抛出错误，会重试 n - 1 次。
            if(student.getAge() == 31) {
                throw new RuntimeException("【手动抛出错误】【手动确认消费】抛出错误！");
            }
        }
    }


    /*---------------------------------手动模式下的正常队列配置死信队列--------------------------------------------*/
    @RabbitListener(queues = RabbitConfig.NORMAL_QUEUE_NAME, ackMode = "MANUAL")
    public void consumeNormalMsg(Message message, Channel channel) throws IOException {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("【正常消息消费】消息内容为：{}", msg);
            Student student = JSON.parseObject(msg, Student.class);
            log.info("【正常消息消费】学生信息为：{}", JSONUtil.toJsonStr(student));
            if(student.getAge() == 31) {
                throw new RuntimeException("【重试测试】【正常消息消费】抛出错误！");
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e) {
            e.printStackTrace();
            /**
             * 这里不能用basicAck，否则会直接确认丢失的，不会进入死信队列中，必须使用basicNack才会进入死信队列中
             * 使用basicNack会拒绝消费消息（丢失消息） 给死信队列
             */
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

    @RabbitListener(queues = RabbitConfig.DEAD_QUEUE_NAME, ackMode = "MANUAL")
    public void consumeDXLMsg(Message message, Channel channel) throws IOException {
        Student student = null;
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("【死信队列】消息内容为：{}", msg);
            student = JSON.parseObject(msg, Student.class);
            log.info("【死信队列】学生信息为：{}", JSONUtil.toJsonStr(student));
            if(student.getAge() == 31) {
                throw new RuntimeException("【重试测试】【死信队列】抛出错误！");
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e) {
            /**
             * 如果死信队列这里消费失败了，会重试 n - 1 次，重试完了，消息仍然在死信队列中
             */
            e.printStackTrace();
            String keyPrefix = "student_test_cache_";
            redisTemplate.opsForValue().set(keyPrefix + student.getName(), JSONUtil.toJsonStr(student));
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

    /*---------------------------------自动模式下的正常队列配置死信队列--------------------------------------------*/
    /**
     * 自动确认模式下，消费失败，会重试 n - 1 次，如果没有配置死信队列，直接丢失，如果配置了死信队列， 就进入死信队列，
     */

    @RabbitListener(queues = RabbitConfig.NORMAL_QUEUE_TWO_NAME, ackMode = "AUTO")
    public void consumeNormalTwoMsg(Message message, Channel channel) {
        byte[] body = message.getBody();
        String msg = new String(body);
        log.info("【自动确认消费two】消息内容为：{}", msg);
        Student student = JSON.parseObject(msg, Student.class);
        log.info("【自动确认消费two】学生信息为：{}", JSONUtil.toJsonStr(student));
        if(student.getAge() == 31) {
            throw new RuntimeException("【重试测试】【自动确认消费two】抛出错误！");
        }
    }

    @RabbitListener(queues = RabbitConfig.DEAD_QUEUE_TWO_NAME, ackMode = "MANUAL")
    public void consumeDXLTwoMsg(Message message, Channel channel) throws IOException {
        Student student = null;
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("【死信队列two】消息内容为：{}", msg);
            student = JSON.parseObject(msg, Student.class);
            log.info("【死信队列two】学生信息为：{}", JSONUtil.toJsonStr(student));
            if(student.getAge() == 31) {
                throw new RuntimeException("【重试测试】【死信队列two】抛出错误！");
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e) {
            /**
             * 如果死信队列这里消费失败了，会重试 n - 1 次，重试完了，消息仍然在死信队列中
             */
            e.printStackTrace();
            String keyPrefix = "student_test_cache_";
            redisTemplate.opsForValue().set(keyPrefix + student.getName(), JSONUtil.toJsonStr(student));
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }
}
