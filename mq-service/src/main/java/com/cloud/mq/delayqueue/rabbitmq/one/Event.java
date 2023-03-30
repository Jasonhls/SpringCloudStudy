package com.cloud.mq.delayqueue.rabbitmq.one;//package com.cloud.es.delay;

/**
 * 获取延时消息事件接口 根据不同事件 实现事件处理器
 * @author Irving
 * @date 2021/7/8 13:12
 */
public interface Event {

    /**
     * 事件名称
     * @return
     */
    String getEventName();

    /**
     * 事件描述
     * @return
     */
    String getEventDes();
}
