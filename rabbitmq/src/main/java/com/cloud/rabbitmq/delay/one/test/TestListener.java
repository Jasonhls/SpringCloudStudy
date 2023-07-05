package com.cloud.rabbitmq.delay.one.test;//package com.cloud.es.delay;

import com.cloud.mq.delayqueue.rabbitmq.one.DelayMsg;
import com.cloud.mq.delayqueue.rabbitmq.one.TaskEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author shenlihui
 * @date 2021/3/29 20:10
 */
@Component("testMessage")
@Slf4j
public class TestListener implements TaskEventListener {
    @Override
    public void invoke(DelayMsg delayMsg) {
        log.info("接收到的消息：" + delayMsg.getContent());
    }
}
