package com.cloud.rabbitmq.delay.two.receiver;

import com.cloud.mq.delayqueue.rabbitmq.two.DelayMsgListenerAdaptor;
import com.cloud.mq.delayqueue.rabbitmq.two.exception.DelayMsgConsumeFailedException;
import com.cloud.mq.delayqueue.rabbitmq.two.vo.DelayMsg;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
@RequiredArgsConstructor
public class DefaultDelayMsgListenerAdaptor implements DelayMsgListenerAdaptor {
    @NonNull
    @Getter
    private final String topic;
    @NonNull
    private final Object delegate;
    @NonNull
    private final Method method;

    public void onMessage(@NonNull DelayMsg<?> msg) {
        try {
            method.invoke(delegate, msg.getData());
        } catch (InvocationTargetException ex) {
            Throwable targetEx = ex.getTargetException();
            throw new DelayMsgConsumeFailedException(
                    "DelayMsgListener method '" + delegate.getClass().getName() + "#" + method.getName() + "' threw exception", targetEx);
        } catch (Exception ex) {
            throw new DelayMsgConsumeFailedException(
                    "Failed to invoke target method '" + method.getName() + "' with delayMsg = [" + msg + "]", ex);
        }
    }
}
