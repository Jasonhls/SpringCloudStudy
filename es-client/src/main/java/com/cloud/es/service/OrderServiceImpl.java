package com.cloud.es.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.es.mapper.OrderMapper;
import com.cloud.es.pojo.Order;
import org.springframework.stereotype.Service;

/**
 * @Author: 何立森
 * @Date: 2023/03/24/18:30
 * @Description:
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
}
