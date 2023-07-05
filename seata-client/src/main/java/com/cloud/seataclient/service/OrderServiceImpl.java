package com.cloud.seataclient.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.seataclient.mapper.OrderMapper;
import com.cloud.seataclient.pojo.Order;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 何立森
 * @Date: 2023/04/04/15:30
 * @Description:
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void saveOrder(Order order) {
        save(order);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE.toString());
        Map<String, Object> map = new HashMap<>(1);
        map.put("name", "张三");
        map.put("age", 99);
        map.put("set", 1);
        HttpEntity<String> entity = new HttpEntity<>(map.toString(), headers);
        String result = restTemplate.postForObject("localhost:8896/es/student/save", entity, String.class);
        System.out.println(result);
        int i = 1 / 0;
    }
}
