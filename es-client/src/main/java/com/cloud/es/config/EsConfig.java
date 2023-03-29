package com.cloud.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 何立森
 * @Date: 2023/03/24/17:58
 * @Description:
 */
@Configuration
public class EsConfig {
//    @Value("${spring.elasticsearch.rest.uris}")
//    private String esUrl;

    public static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder=RequestOptions.DEFAULT.toBuilder();
        COMMON_OPTIONS = builder.build();
    }
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost("172.31.252.106", 9200, "http"));
        RestHighLevelClient client=new RestHighLevelClient(builder);
        return client;
    }
}
