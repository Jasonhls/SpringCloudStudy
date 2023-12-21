package com.cloud.clientserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: 何立森
 * @Date: 2023/12/14/10:01
 * @Description:
 */
@Configuration
public class ClientConfig {


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 1、RestTemplate默认的clientHttpRequestFactory是SimpleClientHttpRequestFactory对象，
     * 发送http请求的ClientHttpRequest是SimpleBufferingClientHttpRequest对象，connection为java.net.HttpURLConnection。
     *
     * 2、指定ClientHttpRequestFactory类型为OkHttp3ClientHttpRequestFactory作为RestTemplate的clientHttpRequestFactory，
     * 这样发送http请求的clientHttpRequest是OkHttp3ClientHttpRequest对象，其属性OkHttpClient发送请求。
     * @return
     */
    /*@Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        return new RestTemplate(clientHttpRequestFactory);
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        return new OkHttp3ClientHttpRequestFactory();
    }*/

}
