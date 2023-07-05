package com.cloud.rabbitmq.delay.one;//package com.cloud.es.delay;

/**
 * 延时消息处理接口
 * @author Irving
 * @date 2021/7/8 13:15
 */
public interface TaskEventListener {

    /**
     * 执行延迟消息处理任务
     * @param delayMsg
     */
    void invoke(DelayMsg delayMsg) ;
}
