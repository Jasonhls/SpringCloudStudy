package com.cloud.seataclient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.seataclient.pojo.Order;

/**
 * @Author: 何立森
 * @Date: 2023/04/04/15:30
 * @Description:
 */
public interface OrderService extends IService<Order> {
    void saveOrder(Order order);
}
