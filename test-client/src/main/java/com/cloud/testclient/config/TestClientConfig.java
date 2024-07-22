package com.cloud.testclient.config;

import com.cloud.testclient.extend.CustomCommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 何立森
 * @Date: 2024/07/12/14:18
 * @Description:
 */
@Configuration
public class TestClientConfig {

    @Bean
    public CustomCommandLineRunner customCommandLineRunner() {
        return new CustomCommandLineRunner();
    }

}
