package com.cloud.seataclient.controller;

import com.cloud.seataclient.pojo.Order;
import com.cloud.seataclient.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 何立森
 * @Date: 2023/04/04/15:29
 * @Description:
 */
@RestController
@RequestMapping(value = "/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    @PostMapping(value = "/save")
    public void save(@RequestBody Order order) {
        orderService.saveOrder(order);
    }

}
