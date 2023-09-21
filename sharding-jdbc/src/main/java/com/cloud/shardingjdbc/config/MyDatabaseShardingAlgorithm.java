package com.cloud.shardingjdbc.config;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @Author: 何立森
 * @Date: 2023/09/21/14:48
 * @Description:
 */
@Slf4j
public class MyDatabaseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        String columnName = preciseShardingValue.getColumnName();
        Long value = preciseShardingValue.getValue();
        log.info("分库的字段：{}， 字段的值：{}", columnName, value);
        String dbName = "ds" + value % 2;
        log.info("所有的数据库为：{}", JSONUtil.toJsonStr(collection));
        if(!collection.contains(dbName)) {
            throw new RuntimeException("数据库" + dbName + "不存在！");
        }
        return dbName;
    }
}
