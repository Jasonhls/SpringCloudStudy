package com.cloud.rabbitmq.delay.one;//package com.cloud.es.delay;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *  延时队列管理接口
 * @author Irving
 * @date 2021/7/8 13:10
 */
public interface DelayQueueManager {

    /**
     * 添加队列
     * @param delayMsg 延时消息
     * @param delay    时间
     * @param timeUnit 时间单位
     */
    void addQueue(DelayMsg delayMsg, long delay, TimeUnit timeUnit);

    /**
     * 删除队列
     * @param delayMsg
     * @return
     */
    void removeQueue(DelayMsg delayMsg);


    /**
     * 延时消息是否已存在
     * @param delayMsg
     * @return
     */
    boolean isContains(DelayMsg delayMsg);


    /**
     * 判断当前消费延时消息线程是否存活
     *
     * @return
     */
    default boolean listenThreadIsAlive() {
        return true;
    }

    /**
     * 重置监听线程
     */
    default  void resetListenThread() {}


    /**
     * 获取当前队列中的所有未消费的延时消息
     * @return
     */
    default List<DelayMsg> getAllDelayMsg(){
        return Lists.newArrayList();
    }
}
