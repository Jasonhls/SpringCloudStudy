package com.cloud.rabbitmq.delay.one.test;

import com.cloud.mq.delayqueue.rabbitmq.one.DelayMsg;
import com.cloud.mq.delayqueue.rabbitmq.one.DelayQueueManager;
import com.cloud.mq.delayqueue.rabbitmq.one.EventEnum;
import com.cloud.mq.delayqueue.rabbitmq.one.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author sha.li
 * @date 2020-12-14
 */
@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaAutowiredFieldsWarningInspection"})
@RestController
@RequestMapping("test1")
@Slf4j
public class TestController {

    @Autowired
    DelayQueueManager delayQueueManager;



    @GetMapping("addDelayMsg/{content}/{delay}")
    public boolean addDelayMsg(@PathVariable("content") String content, @PathVariable("delay") Long delay) {
        DelayMsg delayMsg = new DelayMsg();
        delayMsg.setContent(content);
        delayMsg.setEvent(EventEnum.TEST);
        delayQueueManager.addQueue(delayMsg, delay, TimeUnit.SECONDS);
        return true;
    }

    @GetMapping("delDelayMsg/{content}")
    public boolean delDelayMsg(@PathVariable("content") String content) {
        DelayMsg delayMsg = new DelayMsg();
        delayMsg.setContent(content);
        delayMsg.setEvent(EventEnum.TEST);
        delayQueueManager.removeQueue(delayMsg);
        return true;
    }

    @GetMapping(value = "/deleteRedisKey")
    public Boolean delete(@RequestParam("key") String key) {
        return RedisUtils.del(key);
    }


}
