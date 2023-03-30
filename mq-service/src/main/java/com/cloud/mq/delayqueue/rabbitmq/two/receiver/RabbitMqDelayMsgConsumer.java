package com.cloud.mq.delayqueue.rabbitmq.two.receiver;

import cn.hutool.core.text.StrPool;
import com.cloud.mq.delayqueue.rabbitmq.two.DelayMsgListenerAdaptor;
import com.cloud.mq.delayqueue.rabbitmq.two.exception.DelayMsgConsumeFailedException;
import com.cloud.mq.delayqueue.rabbitmq.two.sender.DelayMsgRetryer;
import com.cloud.mq.delayqueue.rabbitmq.two.support.Supporter;
import com.cloud.mq.delayqueue.rabbitmq.two.vo.DelayMsg;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BooleanSupplier;

import static cn.hutool.core.util.RandomUtil.randomString;
import static com.cloud.mq.delayqueue.rabbitmq.two.consts.Consts.INFO;
import static com.cloud.mq.delayqueue.rabbitmq.two.consts.Consts.TRACE_ID;
import static com.cloud.mq.delayqueue.rabbitmq.two.converter.DelayMsgConverter.deserializeAutomatic;
import static java.lang.Boolean.TRUE;
import static java.nio.charset.StandardCharsets.UTF_8;


@Slf4j
@RequiredArgsConstructor
public class RabbitMqDelayMsgConsumer implements CommandLineRunner, ChannelAwareMessageListener {
    private final ApplicationContext applicationContext;
    private final RedisTemplate<String, Object> redisTemplate;
    private final String appName;
    private final DelayMsgRetryer delayMsgRetryer;
    /**
     * 生成traceId的时间格式
     */
    private static final DateTimeFormatter TRACE_ID_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMddHHmmssSSS", Locale.getDefault())
            .withZone(ZoneId.systemDefault());

    private final Map<String, DelayMsgListenerAdaptor> adaptors = new HashMap<>();


    @Override
    public void run(String... args) {
        Map<String, DelayMsgListenerAdaptor> beansOfType = applicationContext.getBeansOfType(DelayMsgListenerAdaptor.class);
        beansOfType.forEach((key, value) -> adaptors.put(key.split(StrPool.UNDERLINE)[1], value));
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String jsonString = new String(message.getBody(), UTF_8);
        DelayMsg<?> msg;
        try {
            msg = deserializeAutomatic(jsonString);
        } catch (Exception e) {
            log.error("反序列延迟队列发生错误,jsonStr:" + jsonString, e);
            return;
        }
        try {
            treatTraceId(message);
            log.debug("onMessage received message,jsonString: " + jsonString);
            msg.checkValid();
            doConsume(msg, jsonString);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("DelayMsgConsumeErr,延时任务异常：", e);
            delayMsgRetryer.retry(message, msg, e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    private void treatTraceId(Message message) {
        String traceId = message.getMessageProperties().getHeader(TRACE_ID);
        if (StringUtils.hasText(traceId)) {
            MDC.put(TRACE_ID, traceId);

        }else {
            MDC.put(TRACE_ID, "delayMsg" + LocalDateTime.now().format(TRACE_ID_DATE_FORMATTER) + randomString(4));
        }
    }

    private void doConsume(DelayMsg<?> msg, final String messageStr) {
        final String redisKey = Supporter.getRedisKey(appName, msg);
        switch (msg.getConflictStrategy()) {
            case OVERWRITE:
                consumeByListener(msg, () -> {
                    Object redisVersion = redisTemplate.opsForValue().get(redisKey);
                    if (null == redisVersion) {
                        log.debug(INFO, msg, "canceled");
                        return false;
                    } else {
                        int version = Supporter.readVersion(messageStr);
                        if (version < Integer.parseInt(redisVersion.toString())) {
                            log.debug(INFO, msg, "overwrote");
                            return false;
                        } else {
                            return true;
                        }
                    }
                }, () -> redisTemplate.delete(redisKey));
                break;
            case REPEAT:
                consumeByListener(msg, null, null);
                break;
            case EXCEPTION:
            case IGNORE:
                consumeByListener(msg, () -> {
                    boolean exists = TRUE.equals(redisTemplate.hasKey(redisKey));
                    if (!exists) {
                        log.debug(INFO, msg, "canceled");
                        return false;
                    } else {
                        return true;
                    }
                }, () -> redisTemplate.delete(redisKey));
                break;
            default:
                break;
        }
    }

    /**
     * 消息消费核心调用方法
     * @param msg 消息体
     * @param preCheck 前置处理，返回boolean以决定是否往后进行
     * @param onSuccess 后置处理
     */
    private <T> void consumeByListener(DelayMsg<T> msg, BooleanSupplier preCheck, Runnable onSuccess) {
        if (null != preCheck && !preCheck.getAsBoolean()) {
            return;
        }
        DelayMsgListenerAdaptor listener = adaptors.get(msg.getTopic());
        if (null == listener) {
            throw new DelayMsgConsumeFailedException(
                    " No corresponding DelayMsgListener for topic :{} " + msg.getTopic() + ", originMessage:" + msg);
        }
        else {
            Assert.isTrue(msg.getTopic().equals(listener.getTopic()),
                    "Expected Listener topic to be " + msg.getTopic() + ", but found   " + listener.getTopic());
            listener.onMessage(msg);
        }
        if (null != onSuccess) {
            onSuccess.run();
        }
    }
}
