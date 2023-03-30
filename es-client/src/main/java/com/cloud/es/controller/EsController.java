package com.cloud.es.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.es.config.EsConfig;
import com.cloud.es.pojo.Order;
import com.cloud.es.pojo.OrderAnalysisEventListener;
import com.cloud.es.pojo.OrderExcel2;
import com.cloud.es.service.OrderService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 何立森
 * @Date: 2023/03/24/17:19
 * @Description:
 */
@RestController
@RequestMapping(value = "/es")
@Slf4j
public class EsController {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private OrderService orderService;

    public static final String INDEX = "crm_order_copy";
    public static final String TYPE = "crm_order_copy";

    @PostMapping(value = "/save")
    public void save(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) throws IOException {
//        restHighLevelClient
        DateTime start = DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss");
        DateTime end = DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss");
        QueryWrapper<Order> qw = new QueryWrapper<>();
        qw.lambda().ge(Order::getSubmitDate, start)
            .lt(Order::getSubmitDate, end)
            .orderByAsc(Order::getSubmitDate);
        List<Order> list = orderService.list(qw);
        List<List<Order>> partition = Lists.partition(list, 1000);
        for (List<Order> orderList : partition) {
            boolean flag = batchInsert(orderList);
            log.info("批量插入es中结果：{}", flag);
        }
    }

    public boolean batchInsert(List<Order> list) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("1h");
        for (Order order : list) {
            IndexRequest indexRequest = new IndexRequest(INDEX);
            indexRequest.id(String.valueOf(order.getId()));
            indexRequest.source(JSONUtil.toJsonStr(order), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, EsConfig.COMMON_OPTIONS);
//        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
//            return item.getId();
//        }).collect(Collectors.toList());
//        log.info("内容索引生成:{}", collect);
        return bulk.hasFailures();
    }

    @PostMapping("importOrders")
    public Integer importOrders(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        // 匿名内部类 不用额外写一个DemoDataListener
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        InputStream is = multipartFile.getInputStream();
        OrderAnalysisEventListener orderAnalysisEventListener = new OrderAnalysisEventListener();
        EasyExcel.read(is, OrderExcel2.class, orderAnalysisEventListener)
                .sheet(0)
                .headRowNumber(3)
                .doReadSync();
        is.close();
        List<OrderExcel2> successList = orderAnalysisEventListener.getResultList();
        return successList.size();
    }

}
