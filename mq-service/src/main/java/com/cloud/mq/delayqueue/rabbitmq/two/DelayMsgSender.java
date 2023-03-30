package com.cloud.mq.delayqueue.rabbitmq.two;

import com.cloud.mq.delayqueue.rabbitmq.two.consts.ConflictStrategy;
import com.cloud.mq.delayqueue.rabbitmq.two.vo.DelayMsg;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.time.Duration;

import static cn.hutool.extra.spring.SpringUtil.getApplicationName;
import static java.time.Duration.ZERO;

/**
 *  延时队列管理接口
 */
public interface DelayMsgSender {

    @NonNull
    String currentAppName();

    /**
     * 发送延迟消息
     */
    default <T> void send(@NotBlank String topic, @NonNull T data, @NonNull Duration duration) {
        send(new DelayMsg<>(topic, data, duration));
    }

    /**
     * 发送延迟消息
     */
    default <T> void send(@NotBlank String topic, @NonNull T data, @NonNull Duration duration, ConflictStrategy strategy) {
        send(new DelayMsg<>(topic, data, duration, strategy));
    }

    /**
     * 发送延迟消息
     */
    default void send(@NonNull DelayMsg<?> msg) {
        sendTo(getApplicationName(), msg);
    }

    /**
     * 发送延迟消息给其他服务
     * @param appName 要发送给的appName
     */
    default <T> void sendTo(@NotBlank String appName, @NotBlank String topic, @NonNull T data, @NonNull Duration duration,
            ConflictStrategy strategy) {
        sendTo(appName, new DelayMsg<>(topic, data, duration, strategy));
    }

    /**
     * 发送延迟消息给其他服务
     * @param appName 要发送给的appName
     */
    default <T> void sendTo(@NotBlank String appName, @NotBlank String topic, @NonNull T data, @NonNull Duration duration) {
        sendTo(appName, new DelayMsg<>(topic, data, duration));
    }

    /**
     * 发送延迟消息给其他服务
     * @param appName 要发送给的appName
     * @param msg 消息对象
     */
    void sendTo(@NotBlank String appName, @NonNull DelayMsg<?> msg);

    /**
     * 撤回一个的消息
     * @return boolean 是否成功撤回（是否延迟队列中有要删除的内容，如果没有，撤回会返回false）
     */
    default <T> boolean revoke(@NotBlank String topic, @NonNull T data) {
        return revokeFrom(currentAppName(), new DelayMsg<>(topic, data, ZERO));
    }

    /**
     * 撤回一个消息
     * @param msg 消息
     * @return boolean 是否成功撤回（是否延迟队列中有要删除的内容，如果没有，撤回会返回false）
     */
    default <T> boolean revoke(@NonNull DelayMsg<T> msg) {
        return revokeFrom(currentAppName(), msg);
    }

    /**
     * 从指定服务撤回一个消息
     * @param appName 要撤回消息的所属app名称
     * @return boolean 是否成功删除（是否延迟队列中有要删除的内容，如果没有，删除会返回false）
     */
    default <T> boolean revokeFrom(@NotBlank String appName, @NotBlank String topic, @NonNull T data) {
        return revokeFrom(appName, new DelayMsg<>(topic, data, ZERO));
    }

    /**
     * 从指定服务撤回一个消息
     * @param appName 要撤回消息的所属app名称
     * @param msg 消息对象
     * @return boolean 是否成功删除（是否延迟队列中有要删除的内容，如果没有，删除会返回false）
     */
    <T> boolean revokeFrom(@NotBlank String appName, @NonNull DelayMsg<T> msg);

    /**
     * 判断延时消息是否已存在
     */
    default <T> boolean exists(@NotBlank String topic, @NonNull T data) {
        return exists(currentAppName(), new DelayMsg<>(topic, data, ZERO));
    }

    /**
     * 判断延时消息是否已存在
     * @param msg 消息对象
     */
    default boolean exists(@NonNull DelayMsg<?> msg) {
        return exists(currentAppName(), msg);
    }

    /**
     * 判断延时消息是否已存在
     * @param appName 消息所在app名称
     */
    default <T> boolean exists(@NotBlank String appName, @NotBlank String topic, @NonNull T data) {
        return exists(appName, new DelayMsg<>(topic, data, ZERO));
    }

    /**
     * 判断延时消息是否已存在
     * @param appName 消息所在app名称
     * @param msg 消息对象
     */
    boolean exists(@NotBlank String appName, @NonNull DelayMsg<?> msg);
}
