package com.cloud.mq.delayqueue.rabbitmq.two.config;

import com.cloud.mq.delayqueue.rabbitmq.two.DelayMsgListener;
import com.cloud.mq.delayqueue.rabbitmq.two.DelayMsgListenerAdaptor;
import com.cloud.mq.delayqueue.rabbitmq.two.exception.DelayMsgListenerDefineException;
import com.cloud.mq.delayqueue.rabbitmq.two.receiver.DefaultDelayMsgListenerAdaptor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static cn.hutool.core.text.CharPool.UNDERLINE;
import static com.cloud.mq.delayqueue.rabbitmq.two.consts.Consts.CONFIG_LOG_PRE;
import static java.util.Objects.requireNonNull;


/**
 * 处理 {@link DelayMsgListener DelayMsgListener} 注解
 */
@Slf4j
public class DelayMsgListenerAnnotationBeanPostProcessor implements BeanPostProcessor {
    private final ConfigurableApplicationContext applicationContext;

    public DelayMsgListenerAnnotationBeanPostProcessor(ConfigurableApplicationContext applicationContext){
        this.applicationContext = applicationContext;
        log.info(CONFIG_LOG_PRE + "DelayMsgListenerAnnotationBeanPostProcessor");
    }

    @Override
    @Nullable
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(DelayMsgListener.class)) {
                DelayMsgListener listener = method.getAnnotation(DelayMsgListener.class);
                String topic = requireNonNull(AnnotationUtils.getValue(listener),
                        "延迟消息Listener:[" + method.getName() + "]未配置topic").toString();
                if (!Modifier.isPublic(method.getModifiers()))
                    throw new DelayMsgListenerDefineException("延迟消息[" + topic + "]的监听方法需声明为public");
                if (method.getParameterCount() != 1)
                    throw new DelayMsgListenerDefineException("延迟消息[" + topic + "]的监听方法参数只应是1");
                DefaultDelayMsgListenerAdaptor adaptor = new DefaultDelayMsgListenerAdaptor(topic, bean, method);
                String listenerBeanName = DelayMsgListenerAdaptor.class.getName() + UNDERLINE + topic;
                log.debug("register delayMsg listener " + listenerBeanName);
                beanFactory.registerSingleton(listenerBeanName, adaptor);
            }
        }
        return bean;
    }
}
