package com.cloud.mq.delayqueue.rabbitmq.one;//package com.cloud.es.delay;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
/**
 * delay msg on rabbitmq-delayed-message-exchange plugin
 * @author Irving
 * @date 2021/7/8 13:32
 */
@Slf4j
@Component("rabbitmqDelayQueueManager")
@ConditionalOnProperty(value = "delay-queue.rabbitmq.enabled", havingValue = "true")
public class RabbitMqDelayQueueManager implements DelayQueueManager{

    private static final String PREFIX = "rabbitmq:trigger:{}:{}";

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMqDelayProperties rabbitMqDelayProperties;


    @Override
    public void addQueue(DelayMsg delayMsg, long delay, TimeUnit timeUnit) {
        if(delayMsg==null || timeUnit==null){
            log.warn("addQueue rabbitmq fail param is empty");
            return;
        }
        if(StringUtils.isEmpty(delayMsg.getContent()) || delayMsg.getEvent()==null){
            log.warn("addQueue rabbitmq fail delayMsg param is empty");
            return;
        }
        String taskUniqueKey=generateTaskUniqueKey(delayMsg);
        log.info("taskUniqueKey {}", taskUniqueKey);
        if(RedisUtils.hasKey(taskUniqueKey)){
            log.warn("addQueue rabbitmq delayMsg:{} is exists", delayMsg);
            return;
        }
        log.info("addQueue rabbitmq delayMsg:{} delay:{}", delayMsg, delay);

        //标识任务需要执行
        RedisUtils.set(taskUniqueKey,1);

        rabbitTemplate.convertAndSend(rabbitMqDelayProperties.getXdelayExchange(), rabbitMqDelayProperties.getXdelayQueue(), delayMsg, message -> {

            //如果执行的延时任务应该是在现在日期之前执行的，那么补救一下，要求系统一秒钟后执行
            if (delay < 0) {
                message.getMessageProperties().setHeader("x-delay", 1000);
            }else{
                message.getMessageProperties().setHeader("x-delay", timeUnit.toMillis(delay));
            }
            return message;
        });
    }

    @Override
    public void removeQueue(DelayMsg delayMsg) {
        RedisUtils.del(generateTaskUniqueKey(delayMsg));
    }

    @Override
    public boolean isContains(DelayMsg delayMsg) {
        return RedisUtils.hasKey(generateTaskUniqueKey(delayMsg));
    }

    private static String generateTaskUniqueKey(DelayMsg delayMsg){
        //此处生成延时任务唯一key的方式 某些场景可能会存在问题 同一个event下 delayMsg hashCode重复 较晚时间放入队列的延时消息会永远不会被消费
        return StrFormatter.format(PREFIX,delayMsg.hashCode(),delayMsg.getEvent().getEventName());
    }


    @Slf4j
    @Component
    @ConditionalOnProperty(value = "delay-queue.rabbitmq.enabled", havingValue = "true")
    public static class RabbitmqDelayMsgConsumer{
        /**
         * 接收消息，监听 CONSUMPTION_QUEUE 队列
         */
        @RabbitListener(queuesToDeclare = @Queue("#{rabbitMqDelayProperties.xdelayQueue}"),ackMode = "AUTO")
        public void consume(DelayMsg delayMsg) {

            try {
                String key =generateTaskUniqueKey(delayMsg);
                log.info("key {}", key);
                //如果这个任务被标识不执行
                if (RedisUtils.get(key) == null) {
                    log.info("延时消息执行被取消 {} |任务标识：{} body:【{}】 ",delayMsg.getEvent().getEventDes(),key, delayMsg);
                    return;
                }
                log.info("rabbitmqDelayMsgConsumer :" + delayMsg.getEvent().getEventDes());
                log.info("延时消息参数：" + delayMsg);

                //执行任务前 清除标识
                RedisUtils.del(key);

                TaskEventListener taskEventListener =SpringUtil.getBean(delayMsg.getEvent().getEventName());
                taskEventListener.invoke(delayMsg);
            } catch (Exception e) {
                log.error("延时任务异常：", e);
            }
        }

    }
}
