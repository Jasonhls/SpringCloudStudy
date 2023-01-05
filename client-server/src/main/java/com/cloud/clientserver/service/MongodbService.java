package com.cloud.clientserver.service;

import com.cloud.clientserver.pojo.InnerOrder;
import com.cloud.clientserver.util.FieldUtils;
import com.mongodb.bulk.BulkWriteResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class MongodbService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveOrUpdate(List<InnerOrder> list) {
        String collectionName = "scrm_order_full_field_" + list.get(0).getSystemCode();
        List<Pair<Query, Update>> pairList = new ArrayList<>();
        list.forEach(innerOrder -> {
            Query query = new Query();
            Update update = new Update();
            query.addCriteria(Criteria.where("_id").is(innerOrder.getOrderCode()));
            for (Map.Entry<String, Object> entry : innerOrder.entrySet()) {
                if(StringUtils.isNotBlank(entry.getKey())) {
                    if(Objects.nonNull(entry.getValue())) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (FieldUtils.isBooleanField(InnerOrder.class, key)
                                || value instanceof Boolean) {
                            // 保存数值0 1
                            update.set(key, Boolean.valueOf(value.toString()) ? 1 : 0);
                        } else if (FieldUtils.isIntegerField(InnerOrder.class, key)
                                || value instanceof Integer) {
                            // 数字类型需要保存为数字
                            update.set(key, Integer.valueOf(value.toString()));
                        } else if (FieldUtils.isLongField(InnerOrder.class, key) || value instanceof Long) {
                            // 数字类型需要保存为数字
                            update.set(key, Long.valueOf(value.toString()));
                        } else if (FieldUtils.isBigDecimalField(InnerOrder.class, key)
                                || value instanceof BigDecimal) {
                            // BigDecimal类型需要保存为Double
                            update.set(key, Double.valueOf(value.toString()));
                        } else {
                            update.set(key, value);
                        }
                    }
                }
            }
            update.set("_id", innerOrder.getOrderCode());
            Document document = update.getUpdateObject();
            Map map = document.get("$set", Map.class);
            //如果有更新的字段，就添加进来
            if(!map.isEmpty()) {
                pairList.add(Pair.of(query, update));
            }
        });
        upsert(collectionName, pairList);
    }

    private BulkWriteResult upsert(String collectionName, List<Pair<Query, Update>> pairList) {
        BulkOperations operations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, collectionName);
        operations.upsert(pairList);
        BulkWriteResult result;
        try {
            result = operations.execute();
        } catch (Exception e) {
            // 自动重试一次
            result = operations.execute();
            log.error("数据同步异常", e);
        }
        return result;
    }
}
