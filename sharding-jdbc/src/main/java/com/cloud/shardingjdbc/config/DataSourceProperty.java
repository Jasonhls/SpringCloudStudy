package com.cloud.shardingjdbc.config;

/**
 * @Author: 何立森
 * @Date: 2023/09/25/17:23
 * @Description:
 */

import lombok.Getter;
import lombok.Setter;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DataSourceProperty {
    /**
     * 登录用户名
     */
    private String loginUsername;
    /**
     * 登录密码
     */
    private String loginPassword;
    /**
     * 是否能够重置数据 禁用HTML页面上的“Reset All”功能
     */
    private Boolean resetEnable = false;

    private String url;
    private String username;
    private String password;

    private Integer initialSize;
    private Integer minIdle;
    private Integer maxActive;
    private Long maxWait;
    private Long timeBetweenEvictionRunsMillis;
    private Long minEvictableIdleTimeMillis;
    private String validationQuery;
    private Boolean testWhileIdle;
    private Boolean testOnBorrow;
    private Boolean testOnReturn;
    private Boolean poolPreparedStatements;
    private Integer maxPoolPreparedStatementPerConnectionSize;
    private String filters;
    private Boolean useGlobalDataSourceStat;
    private String connectProperties;
    private Boolean mergeSql;

}

