package com.cloud.clientserver.custom;

import com.cloud.clientserver.enums.IEnum;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @Author: 何立森
 * @Date: 2023/01/16/14:59
 * @Description: 自定义反序列化
 */
public class BaseDeserializer extends JsonDeserializer<IEnum> {

    @Override
    public IEnum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            //前端输入的值
            String text = jsonParser.getText();
            if(StringUtils.isBlank(text)) {
                return null;
            }
            JsonStreamContext parsingContext = jsonParser.getParsingContext();
            //字段名
            String currentName = parsingContext.getCurrentName();
            //前端注入的对象
            Object currentValue = parsingContext.getCurrentValue();
            Field field = ReflectionUtils.findField(currentValue.getClass(), currentName);
            //获取对应的枚举类

            Class<IEnum> enumClass = (Class<IEnum>)field.getType();
            //根据对应的值和枚举类获取相应的枚举值
            return IEnum.codeOf(enumClass, Integer.parseInt(text));
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
