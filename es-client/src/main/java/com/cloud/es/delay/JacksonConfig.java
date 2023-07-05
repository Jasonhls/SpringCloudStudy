//package com.cloud.es.delay;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.TimeZone;
//
///**
// * jackson 配置
// *
// * @author sha.li
// * @date 2020-12-14
// */
//@SuppressWarnings("SpringFacetCodeInspection")
//@Configuration
//public class JacksonConfig {
//    private static final Logger log = LoggerFactory.getLogger(JacksonConfig.class);
//    private static final String STANDARD_PATTERN = "yyyy-MM-dd HH:mm:ss";
//    private static final String DATE_PATTERN = "yyyy-MM-dd";
//    private static final String TIME_PATTERN = "HH:mm:ss";
//    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();
//    private static final ObjectMapper REDIS_OBJECT_MAPPER = new ObjectMapper();
//
//    static {
//        {
////            log.info(CommonData.CONFIG_LOG_PRE + "DefaultObjectMapper");
//            doJacksonConfiguration(DEFAULT_OBJECT_MAPPER);
//            SimpleModule module = new SimpleModule();
////            module.addSerializer(IEnum.class, EnumSerializer.INSTANCE);
////            module.addDeserializer(Enum.class, IEnumDeSerializer.getInstance());
//            DEFAULT_OBJECT_MAPPER.registerModule(module);
//        }
//        {
////            log.info(CommonData.CONFIG_LOG_PRE + "RedisObjectMapper");
//            doJacksonConfiguration(REDIS_OBJECT_MAPPER);
//            // 序列化时忽略 空对象（空集合&数组、空字符、空对象）
//            REDIS_OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        }
//    }
//
//    public static ObjectMapper getDefaultObjectMapper() {
//        return DEFAULT_OBJECT_MAPPER;
//    }
//
//    public static ObjectMapper getRedisObjectMapper() {
//        return REDIS_OBJECT_MAPPER;
//    }
//
//    public static void doJacksonConfiguration(ObjectMapper objectMapper) {
//        //设置java.util.Date时间类的序列化以及反序列化的格式
//        objectMapper.setDateFormat(new SimpleDateFormat(STANDARD_PATTERN));
//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        {
//            //处理LocalDateTime
//            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STANDARD_PATTERN);
//            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
//            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
//            //处理LocalDate
//            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
//            javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
//            javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
//            //处理LocalTime
//            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
//            javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
//            javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
//        }
//        {
//            SimpleModule simpleModule = new SimpleModule();
//            ToStringSerializer toStringSerializer = ToStringSerializer.instance;
//            simpleModule.addSerializer(Long.class, toStringSerializer);
//            simpleModule.addSerializer(Long.TYPE, toStringSerializer);
//            simpleModule.addSerializer(Double.class, toStringSerializer);
//            simpleModule.addSerializer(Double.TYPE, toStringSerializer);
//            simpleModule.addSerializer(Float.class, toStringSerializer);
//            simpleModule.addSerializer(Float.TYPE, toStringSerializer);
//            simpleModule.addSerializer(BigDecimal.class, toStringSerializer);
//            simpleModule.addSerializer(BigInteger.class, toStringSerializer);
//            objectMapper.registerModules(javaTimeModule, simpleModule);
//        }
////        {
////            objectMapper.setVisibility(objectMapper.getSerializationConfig()
////                    .getDefaultVisibilityChecker()
////                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
////                    .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
////                    .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
////                    .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
////                    .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
////        }
//        // 在序列化一个空对象时时不抛出异常
//        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//        // 忽略反序列化时在json字符串中存在, 但在java对象中不存在的属性
//        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        // 设置时区
//        objectMapper.setTimeZone(TimeZone.getDefault());
//    }
//
//    /**
//     * mvc使用咱们自定义的objectMapper
//     */
//    @Bean
//    public ObjectMapper jacksonConfiguration() {
//        return DEFAULT_OBJECT_MAPPER;
//    }
//
//}
