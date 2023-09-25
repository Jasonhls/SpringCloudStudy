package com.cloud.shardingjdbc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.shardingjdbc.entity.TbOrder;
import com.cloud.shardingjdbc.param.OrderListParam;
import com.cloud.shardingjdbc.param.OrderParam;
import com.cloud.shardingjdbc.result.OrderResult;
import com.github.pagehelper.PageInfo;

/**
 * @Author: 何立森
 * @Date: 2023/09/08/17:19
 * @Description:
 */
public interface OrderService extends IService<TbOrder> {
    PageInfo<OrderResult> pageList(OrderListParam param);

    void saveOrUpdateOrder(OrderParam orderParam);

    OrderResult selectOrderDetail(Long orderId);
}
