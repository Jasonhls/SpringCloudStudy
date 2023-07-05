package com.cloud.es.delay;

//import cn.hutool.json.JSONUtil;
//import com.cloud.es.pojo.MsgDto;
//import lombok.extern.slf4j.Slf4j;
//import net.trueland.tcloud.scrm.common.delaymsgqueue.AbstractDelayMsgSender;
//import net.trueland.tcloud.scrm.common.delaymsgqueue.vo.DelayMsg;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

/**
 * @Author: 何立森
 * @Date: 2023/03/30/14:30
 * @Description:
 */
//@RestController
//@RequestMapping(value = "/message")
//@Slf4j
//public class MessageController {
//    @Autowired
//    private AbstractDelayMsgSender delayMsgSender;
//
//    @PostMapping(value = "/sendMessage")
//    public void sendMessage(@RequestBody MsgDto msgDto) {
//        DelayMsg<MsgDto> msg = new DelayMsg<>("orderPayTest", msgDto, Duration.ofSeconds(5));
//        log.info("发送的msg内容为：{}", JSONUtil.toJsonStr(msg));
//        delayMsgSender.send(msg);
//        log.info("成功发送延迟消息");
//    }
//
//}
