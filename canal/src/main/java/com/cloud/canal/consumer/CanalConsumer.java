package com.cloud.canal.consumer;

import cn.hutool.json.JSONUtil;
import com.cloud.canal.pojo.CanalMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: 何立森
 * @Date: 2023/11/14/16:11
 * @Description:
 */
@Component
@Slf4j
public class CanalConsumer {

    @RabbitListener(
            bindings = {
                @QueueBinding(value = @Queue(value = "canal_queue", durable = "true"),
                exchange = @Exchange(value = "canal_exchange"),
                key = "canal_routing_key")
            },
            ackMode = "MANUAL"
    )
    public void handleDataChange(Message message, Channel channel) throws IOException {
        //将message转换为CanalMessage
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            //msg样子为：{"data":[{"id":"1720349049869471748","name":"canal学生003","age":"30","sex":"1","create_at":"2023-11-14 20:01:47"}],"database":"test","es":1699963307000,"id":1,"isDdl":false,"mysqlType":{"id":"bigint","name":"varchar(50)","age":"int","sex":"tinyint","create_at":"datetime"},"old":null,"pkNames":["id"],"sql":"","sqlType":{"id":-5,"name":12,"age":4,"sex":-6,"create_at":93},"table":"t_student","ts":1699963876649,"type":"INSERT"}
            log.info("接受到的canal消息为：{}", msg);
            CanalMessage canalMessage = JSONUtil.toBean(msg, CanalMessage.class);
            String table = canalMessage.getTable();
            if(table.equals("t_student")) {
                log.info("学生表信息变化了");
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e) {
            e.printStackTrace();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }

    }
}
