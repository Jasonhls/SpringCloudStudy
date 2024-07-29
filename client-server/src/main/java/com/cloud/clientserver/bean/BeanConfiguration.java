package com.cloud.clientserver.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @Author: 何立森
 * @Date: 2024/07/26/11:03
 * @Description:
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public Person teacher() {
        return new Teacher();
    }

    @Bean
    @Primary
    public Person student() {
        return new Student();
    }
}
