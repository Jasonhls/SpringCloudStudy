//package com.cloud.es.delay;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializer;
//
///**
// * redis 配置
// *
// * @author sha.li
// * @since 2020-11-27
// */
//@Slf4j
//@Configuration
//public class RedisConfig {
//
//    /**
//     * redisTemplate
//     */
//    @Bean
//    @Primary
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//        log.info("配置================> " + "RedisUtils");
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
//        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        ObjectMapper redisObjectMapper = JacksonConfig.getRedisObjectMapper();
//        serializer.setObjectMapper(redisObjectMapper);
//        template.setValueSerializer(serializer);
//        // 使用StringRedisSerializer来序列化和反序列化redis的key值
//        template.setKeySerializer(RedisSerializer.string());
//        template.setHashKeySerializer(serializer);
//        template.setHashValueSerializer(serializer);
//        template.afterPropertiesSet();
//        RedisUtils.init(template);
//        return template;
//    }
//
//    @Bean
//    @ConditionalOnBean(name = "redisTemplate")
//    public StringRedisTemplate redisTemplateString(RedisConnectionFactory redisConnectionFactory) {
//        StringRedisTemplate template = new StringRedisTemplate();
//        template.setConnectionFactory(redisConnectionFactory);
//        return template;
//    }
//}
