package com.cloud.shardingjdbc.controller;

import cn.hutool.json.JSONUtil;
import com.cloud.shardingjdbc.param.OrderListParam;
import com.cloud.shardingjdbc.param.OrderParam;
import com.cloud.shardingjdbc.result.OrderResult;
import com.cloud.shardingjdbc.service.OrderService;
import com.cloud.shardingjdbc.utils.DataSourceUtils;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 何立森
 * @Date: 2023/09/08/16:27
 * @Description:
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/saveOrUpdateOrder")
    public void saveOrUpdateOrder(@RequestBody OrderParam orderParam) {
        log.info("新增或编辑订单的入参为：{}", JSONUtil.toJsonStr(orderParam));
        orderService.saveOrUpdateOrder(orderParam);
    }

    @GetMapping(value = "/orderDetail/{orderId}")
    public OrderResult selectOrderDetail(@PathVariable(value = "orderId") Long orderId) {
//        DataSourceUtils.switchDataSource(orderId);
        return orderService.selectOrderDetail(orderId);
    }

    @PostMapping(value = "/pageList")
    public PageInfo<OrderResult> pageList(@RequestBody OrderListParam param) {
        return orderService.pageList(param);
    }
}
