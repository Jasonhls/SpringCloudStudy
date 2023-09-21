package com.cloud.shardingjdbc.config;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @Author: 何立森
 * @Date: 2023/09/08/17:51
 * @Description: 自定义分表策略
 */
@Slf4j
public class MyTableShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        //分片主键id
        String columnName = preciseShardingValue.getColumnName();
        Long value = preciseShardingValue.getValue();
        log.info("分表的字段：{}, 字段的值：{}", columnName, value);
        //根据id对2取余，因为总共就两个表
        String tableName = preciseShardingValue.getLogicTableName() + "_" + value % 2;
        log.info("所有的表为：{}", JSONUtil.toJsonStr(collection));
        if(!collection.contains(tableName)) {
            throw new RuntimeException("数据表" + tableName + "不存在！");
        }
        return tableName;
    }
}
