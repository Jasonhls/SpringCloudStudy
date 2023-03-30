package com.cloud.mq.delayqueue.rabbitmq.one;//package com.cloud.es.delay;

import com.google.common.base.Objects;
import lombok.Data;

import java.io.Serializable;

/**
 * 延时消息负载主体
 * @author Irving
 * @date 2021/7/8 13:13
 */
@Data
public class DelayMsg implements Serializable {

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息所属事件
     */
    private Event event;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DelayMsg delayMsg = (DelayMsg) o;
        return Objects.equal(content, delayMsg.content) &&
                event == delayMsg.event;
    }

    @Override
    public int hashCode() {
        return (this.content + (null == this.event ? "" : this.event.getEventName())).hashCode();
    }


}
