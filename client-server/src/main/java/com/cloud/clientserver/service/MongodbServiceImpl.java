package com.cloud.clientserver.service;

import com.cloud.clientserver.pojo.InnerOrder;
import com.cloud.clientserver.pojo.InnerOrderParam;
import com.cloud.clientserver.util.FieldUtils;
import com.github.pagehelper.PageInfo;
import com.mongodb.bulk.BulkWriteResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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
import java.util.regex.Pattern;

@Service
@Slf4j
public class MongodbServiceImpl implements MongodbService{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
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


    @Override
    public PageInfo<InnerOrder> list(InnerOrderParam param) {
        String collectionName = "scrm_order_full_field_" + param.getSystemCode();
        Criteria criteria = new Criteria();
        criteria.and("systemCode").is(param.getSystemCode());
        /**
         * 模糊匹配
         * 模糊匹配：^.* + phone + .*$
         * 右匹配：^.* + phone + $
         * 左匹配：^ + phone + .*$
         */
        if(StringUtils.isNotBlank(param.getCustomerName())) {
            criteria.and("customerName").regex("^.*" + param.getCustomerName() + ".*$");
        }
        if(StringUtils.isNotBlank(param.getOrderCode())) {
            criteria.and("orderCode").regex("^.*" + param.getOrderCode() + ".*$");
        }
        if(StringUtils.isNotBlank(param.getTel())) {
            criteria.and("tel").regex("^.*" + param.getTel() + ".*$");
        }
        //条件筛选
        AggregationOperation matchOperation = Aggregation.match(criteria);
        //分组操作
        /*AggregationOperation groupOperation = Aggregation.group("orderCode", "orderMoney")
                .count().as("count")
                .max("age").as("max")
                .min("age").as("min");*/
        //选择字段
        /*AggregationOperation projectOperation = Aggregation.project().and("tel").as("buyerPhone")
                .andExclude("systemCode", "orderCode", "orderMoney", "buyerName", "customerId", "customerName");*/

        //查询总数
        AggregationOperation countOperation = Aggregation.count().as("total");
        Aggregation countAggregation = Aggregation.newAggregation(matchOperation, countOperation);
        AggregationResults<Map> result = mongoTemplate.aggregate(countAggregation, collectionName, Map.class);
        Integer num = (Integer) result.getMappedResults().get(0).get("total");
        long total = num;


        //排序操作
        AggregationOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "createAt");
        //跳过条数
        long skipNum = (long) (param.getPageNum() - 1) * param.getPageSize();
        AggregationOperation skipOperation = Aggregation.skip(skipNum);
        //选择条数
        AggregationOperation limitOperation = Aggregation.limit((long)param.getPageSize());
        /*Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation, projectOperation,
                sortOperation, skipOperation, limitOperation);*/

        //这里也可以添加到List中，几种传入
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, sortOperation, skipOperation, limitOperation);

        AggregationResults<InnerOrder> aggregate = mongoTemplate.aggregate(aggregation, collectionName, InnerOrder.class);
        List<InnerOrder> list = aggregate.getMappedResults();
        PageInfo<InnerOrder> pageInfo = new PageInfo<>(list);
        this.buildPageInfo(pageInfo, param.getPageNum(), param.getPageSize(), total);
        return pageInfo;
    }

    private void buildPageInfo(PageInfo pageInfo, int pageNum, int pageSize, long total) {
        pageInfo.setTotal(total);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        int pages = (int) (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
        pageInfo.setPages(pages);
        pageInfo.calcByNavigatePages(8);
    }
}
