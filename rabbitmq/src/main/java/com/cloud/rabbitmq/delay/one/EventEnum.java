package com.cloud.rabbitmq.delay.one;//package com.cloud.es.delay;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: tcloud_crm
 * @description: 相关事件枚举
 * @author: yan.zhang
 * @create: 2022-10-31 14:21
 **/
@AllArgsConstructor
@Getter
public enum EventEnum implements Event {

    /**
     * 发送员工订单信息的企微消息
     */
    TEST("testMessage", "test"),

    ;


    /**
     * 对应TaskEventListener beanName
     */
    private final String eventName;
    private final String eventDes;
}
