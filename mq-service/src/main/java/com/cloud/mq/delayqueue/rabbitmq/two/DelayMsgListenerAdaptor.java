package com.cloud.mq.delayqueue.rabbitmq.two;

import com.cloud.mq.delayqueue.rabbitmq.two.vo.DelayMsg;
import lombok.NonNull;

public interface DelayMsgListenerAdaptor {
    /**
     * 监听主题
     */
    String getTopic();

    /**
     * 消息的主处理方法，调用项目中配置的listener指向方法
     */
    void onMessage(@NonNull DelayMsg<?> msg);
}
