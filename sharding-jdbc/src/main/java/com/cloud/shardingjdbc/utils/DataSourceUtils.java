package com.cloud.shardingjdbc.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.cloud.shardingjdbc.config.MyDynamicDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

/**
 * @Author: 何立森
 * @Date: 2023/09/25/17:36
 * @Description:
 */
@Slf4j
public class DataSourceUtils {

    private static final String SHARDING_DYNAMIC = "sharding_dynamic";
    private static final String SPRING_PROFILE_ACTIVE = "spring.profile.active";

    public static void switchDataSource(Long id) {
        Environment env = SpringUtil.getBean(Environment.class);
        if(SHARDING_DYNAMIC.equals(env.getProperty(SPRING_PROFILE_ACTIVE))) {
            String dataSourceName = MyDynamicDataSource.DATASOURCE_PREFIX + id % 2;
            log.info("当前数据源为：{}", dataSourceName);
            MyDynamicDataSource.setDataSource(dataSourceName);
        }else {
            log.info("未开启动态数据源");
        }
    }
}
