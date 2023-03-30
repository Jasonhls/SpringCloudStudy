package com.cloud.mq.delayqueue.rabbitmq.two.converter;

import cn.hutool.core.lang.Pair;
import com.cloud.mq.delayqueue.rabbitmq.two.consts.Consts;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.NonNull;
import lombok.SneakyThrows;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.cloud.mq.delayqueue.rabbitmq.two.consts.Consts.STANDARD_PATTERN;
import static com.cloud.mq.delayqueue.rabbitmq.two.consts.Consts.TIME_PATTERN;
import static java.util.Objects.requireNonNull;

/**
 * 延迟消息序列器
 */
public class DelayMsgConverter {
    private static final ObjectMapper OBJECT_MAPPER;

    private DelayMsgConverter() {
    }

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        //设置java.util.Date时间类的序列化以及反序列化的格式
        objectMapper.setDateFormat(new SimpleDateFormat(STANDARD_PATTERN));
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        //处理LocalDateTime
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STANDARD_PATTERN);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        //处理LocalDate
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Consts.DATE_PATTERN);
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        //处理LocalTime
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
        objectMapper.registerModules(javaTimeModule);
        //加入类型信息
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        // 序列化时忽略 空对象（空集合&数组、空字符、空对象）
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 忽略反序列化时在json字符串中存在, 但在java对象中不存在的属性
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER = objectMapper;
    }


    /**
     * 序列化内容，用在消息传输、哈希计算上
     */
    @SneakyThrows
    public static <T> String serialize(@NonNull T data) {
        return OBJECT_MAPPER.writeValueAsString(data);
    }


    /**
     * 序列化内容，用在消息传输、哈希计算上
     * @param data java对象
     * @param appendValues 附加内容，只能是基础类型（数值、字符串）
     */
    @SafeVarargs
    @SneakyThrows
    public static <T> String serializeWithAppendValues(@NonNull T data, @NonNull Pair<String,Object>... appendValues) {
        JsonNode rootNode = OBJECT_MAPPER.valueToTree(data);
        ObjectNode objectNode = rootNode.get(1).requireNonNull();
        for (Pair<String, Object> appendValue : appendValues){
            if(null != appendValue.getValue())
                putBasicTypeValue(objectNode, appendValue.getKey(), appendValue.getValue());
        }
        return rootNode.toString();
    }

    /**
     * 从序列化字符串中读取附加信息
     */
    @SneakyThrows
    public static Map<String,String> readAppendValue(@NotBlank String jsonValue, @NonNull String... keys) {
        Map<String, String> map = new HashMap<>(keys.length);
        JsonNode rootNode = OBJECT_MAPPER.readTree(jsonValue);
        ObjectNode objectNode = rootNode.get(1).requireNonNull();
        for (String key : keys){
            JsonNode valueNode = objectNode.get(key);
            if (null != valueNode && !valueNode.isNull())
                map.put(key, valueNode.asText());
        }
        return map;
    }

    private static void putBasicTypeValue(ObjectNode node,String key,Object value) {
        Object v = requireNonNull(value);
        if(v instanceof Integer)
            node.put(key, (Integer) v);
        else if(v instanceof Double)
            node.put(key, (Double) v);
        else if(v instanceof Long)
            node.put(key, (Long) v);
        else if(v instanceof Float)
            node.put(key, (Float) v);
        else if(v instanceof Short)
            node.put(key, (Short) v);
        else if(v instanceof byte[])
            node.put(key, (byte[]) v);
        else if(v instanceof Boolean)
            node.put(key, (Boolean) v);
        else if(v instanceof BigDecimal)
            node.put(key, (BigDecimal) v);
        else if(v instanceof String)
            node.put(key, (String) v);
        else
            throw new IllegalArgumentException("Unknown value type : " + v.getClass());
    }

    /**
     * 反序列化消费者接收到的消息体
     */
    @SneakyThrows
    public static <T> T deserialize(@NotBlank String valueString, @NonNull Class<T> clazz) {
        return OBJECT_MAPPER.readValue(valueString, clazz);
    }

    /**
     * 反序列化消费者接收到的消息体,自动识别json中的类型并转换
     */
    @SneakyThrows
    public static <T> T deserializeAutomatic(@NotBlank String valueString) {
        JsonNode root = OBJECT_MAPPER.readTree(valueString);
        Class<?> clazz = Class.forName(root.get(0).requireNonNull().asText());
        //noinspection unchecked
        return (T) OBJECT_MAPPER.treeToValue(root, clazz);
    }

}
