package com.cloud.shardingjdbc.config;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.cloud.shardingjdbc.utils.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: 何立森
 * @Date: 2023/09/25/15:16
 * @Description: 动态切库，一般只适合与租户id隔离这种类型的业务，不适合业务数据的分库分表
 */
@Slf4j
@Component
public class MyDynamicDataSource extends AbstractRoutingDataSource {

    private Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>(64);

    public static final String DATASOURCE_NAME = "dataSourceName";

    public static final String DATASOURCE_PREFIX = "ds";
    public static final String DRIVER_CLASS_NAME = "driver-class-name";
    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    /**
     * 决定使用哪个数据源
     * @return
     */
    @Override
    protected DataSource determineTargetDataSource() {
        Object dataSourceKey = getDataSourceKey();
        if(!dataSourceMap.containsKey(dataSourceKey)) {
            throw new RuntimeException("对应的数据库信息不存在");
        }
        return (DataSource) dataSourceMap.get(dataSourceKey);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSourceKey();
    }

    public static void setDataSource(String dataSourceName) {
        ThreadLocalUtils.set(DATASOURCE_NAME, dataSourceName);
    }

    public Object getDataSourceKey() {
        Object obj = ThreadLocalUtils.get(DATASOURCE_NAME);
        if(obj == null) {
            return "ds0";
        }
        return obj;
    }

    @Override
    public void afterPropertiesSet() {
        Environment env = SpringUtil.getBean(Environment.class);
        String prefix = "spring.datasource." + DATASOURCE_PREFIX;
        int i = 0;
        while(true) {
            //如果配置项不存在，就退出
            if(!env.containsProperty(prefix + i)) {
                break;
            }
            DataSourceProperty dataSourceProperty = SpringUtil.getBean(DataSourceProperty.class);
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setDriverClassName(env.getProperty(prefix + i + "" + DRIVER_CLASS_NAME));
            druidDataSource.setUrl(env.getProperty(prefix + i + "." + URL));
            druidDataSource.setUsername(env.getProperty(prefix + i + "." + USERNAME));
            druidDataSource.setPassword(env.getProperty(prefix + i + "." + PASSWORD));
            druidDataSource.setInitialSize(dataSourceProperty.getInitialSize());
            druidDataSource.setMinIdle(dataSourceProperty.getMinIdle());
            druidDataSource.setMaxActive(dataSourceProperty.getMaxActive());
            druidDataSource.setMaxWait(dataSourceProperty.getMaxWait());
            druidDataSource.setTimeBetweenEvictionRunsMillis(dataSourceProperty.getTimeBetweenEvictionRunsMillis());
            druidDataSource.setMinEvictableIdleTimeMillis(dataSourceProperty.getMinEvictableIdleTimeMillis());
            druidDataSource.setValidationQuery(dataSourceProperty.getValidationQuery());
            druidDataSource.setTestWhileIdle(dataSourceProperty.getTestWhileIdle());
            druidDataSource.setTestOnBorrow(dataSourceProperty.getTestOnBorrow());
            druidDataSource.setConnectionProperties(dataSourceProperty.getConnectProperties());
            druidDataSource.setTestOnReturn(dataSourceProperty.getTestOnReturn());
            druidDataSource.setPoolPreparedStatements(dataSourceProperty.getPoolPreparedStatements());
            dataSourceMap.put(DATASOURCE_PREFIX + i, druidDataSource);
            i++;
        }
    }
}
