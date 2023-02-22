package com.cloud.clientserver.callback;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * @Author: 何立森
 * @Date: 2023/01/28/17:55
 * @Description:
 */
@Slf4j
public class SendCallbackListener implements SendCallback {
    @Override
    public void onSuccess(SendResult sendResult) {
        log.info("CallBackListener on success : " + JSON.toJSONString(sendResult));
    }

    @Override
    public void onException(Throwable throwable) {
        log.error("CallBackListener on exception : ", throwable);
    }
}
