package com.cloud.shardingjdbc.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cloud.shardingjdbc.entity.TbOrder;
import com.cloud.shardingjdbc.param.OrderListParam;
import com.cloud.shardingjdbc.service.OrderService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping(value = "/save")
    public void save(@RequestBody TbOrder tbOrder) {
        tbOrder.setId(IdWorker.getId());
        log.info("新增的订单id为：{}", tbOrder.getId());
        orderService.save(tbOrder);
    }

    @GetMapping(value = "/orderDetail/{orderId}")
    public List<TbOrder> selectOrderList(@PathVariable(value = "orderId") Long orderId) {
        return orderService.listByIds(CollectionUtil.newArrayList(orderId));
    }

    @PostMapping(value = "/pageList")
    public PageInfo<TbOrder> pageList(@RequestBody OrderListParam param) {
        return orderService.pageList(param);
    }
}
