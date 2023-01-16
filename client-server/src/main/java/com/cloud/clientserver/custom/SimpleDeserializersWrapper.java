package com.cloud.clientserver.custom;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.type.ClassKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: 何立森
 * @Date: 2023/01/16/15:16
 * @Description:
 * 正序列化的时候会找父类找接口，按照父类或者接口定义的序列化器来序列化。而反序列化的时候不会。
 * 因此需要：重写SimpleDeserializers的findEnumDeserializer方法，找到自定义的反序列化器
 */
public class SimpleDeserializersWrapper extends SimpleDeserializers {
    static final Logger logger = LoggerFactory.getLogger(SimpleDeserializersWrapper.class);
    @Override
    public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config,
                                                    BeanDescription beanDesc) throws JsonMappingException {
        JsonDeserializer<?> enumDeserializer = super.findEnumDeserializer(type, config, beanDesc);
        if (enumDeserializer != null) {
            return enumDeserializer;
        }
        for (Class<?> typeInterface : type.getInterfaces()) {
            enumDeserializer = this._classMappings.get(new ClassKey(typeInterface));
            if (enumDeserializer != null) {
                logger.info("\n====>重写枚举查找逻辑[{}]",enumDeserializer);
                return enumDeserializer;
            }
        }
        return null;
    }
}
