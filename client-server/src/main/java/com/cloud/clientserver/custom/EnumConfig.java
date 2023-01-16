package com.cloud.clientserver.custom;

import com.cloud.clientserver.enums.IEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @Author: 何立森
 * @Date: 2023/01/16/15:10
 * @Description:
 */
@Configuration
public class EnumConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        SimpleDeserializersWrapper deserializersWrapper = new SimpleDeserializersWrapper();
        deserializersWrapper.addDeserializer(IEnum.class, new BaseDeserializer());
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.setDeserializers(deserializersWrapper);
        simpleModule.addSerializer(IEnum.class, new BaseSerializer());
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }
}
