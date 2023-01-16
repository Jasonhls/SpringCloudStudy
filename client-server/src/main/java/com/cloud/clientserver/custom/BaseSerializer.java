package com.cloud.clientserver.custom;

import com.cloud.clientserver.enums.IEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @Author: 何立森
 * @Date: 2023/01/16/16:32
 * @Description: 自定义正序列化
 */
@Slf4j
public class BaseSerializer extends JsonSerializer<IEnum> {
    @Override
    public void serialize(IEnum iEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        log.info("\n====>开始序列化[{}]", iEnum);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("code", iEnum.getCode());
        jsonGenerator.writeStringField("desc", iEnum.getDesc());
        jsonGenerator.writeEndObject();
    }
}
