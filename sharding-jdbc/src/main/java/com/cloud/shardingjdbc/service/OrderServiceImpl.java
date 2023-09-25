package com.cloud.shardingjdbc.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.shardingjdbc.entity.TbOrder;
import com.cloud.shardingjdbc.entity.TbOrderProduct;
import com.cloud.shardingjdbc.mapper.OrderMapper;
import com.cloud.shardingjdbc.param.OrderListParam;
import com.cloud.shardingjdbc.param.OrderParam;
import com.cloud.shardingjdbc.result.OrderProductResult;
import com.cloud.shardingjdbc.result.OrderResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 何立森
 * @Date: 2023/09/08/17:19
 * @Description:
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, TbOrder> implements OrderService {

    @Autowired
    private OrderProductService orderProductService;

    @Override
    public PageInfo<OrderResult> pageList(OrderListParam param) {
        PageHelper.startPage(param);
        QueryWrapper<TbOrder> qw = new QueryWrapper<>();
        qw.lambda().like(StringUtils.isNotBlank(param.getOrderCode()), TbOrder::getOrderCode, param.getOrderCode())
                .orderByDesc(TbOrder::getCreateTime);
        List<TbOrder> list = list(qw);
        if(CollectionUtil.isEmpty(list)) {
            return PageInfo.of(Collections.EMPTY_LIST);
        }
        List<OrderResult> orderResults = BeanUtil.copyToList(list, OrderResult.class);
        List<Long> orderIds = list.stream().map(TbOrder::getId).collect(Collectors.toList());
        List<TbOrderProduct> orderProducts = orderProductService.selectOrderProducts(orderIds);
        orderResults.forEach(a -> {
            List<TbOrderProduct> products = orderProducts.stream().filter(b -> b.getOrderId().equals(a.getId())).collect(Collectors.toList());
            List<OrderProductResult> orderProductResults = BeanUtil.copyToList(products, OrderProductResult.class);
            a.setOrderProductList(orderProductResults);
        });
        return PageInfo.of(orderResults);
    }

    @Override
    public void saveOrUpdateOrder(OrderParam orderParam) {
        TbOrder tbOrder = BeanUtil.copyProperties(orderParam, TbOrder.class);
        if(orderParam.getId() == null) {
            //新增
            tbOrder.setId(IdWorker.getId());
            List<TbOrderProduct> orderProducts = BeanUtil.copyToList(orderParam.getOrderProductList(), TbOrderProduct.class);
            orderProducts.forEach(a -> {
                a.setId(IdWorker.getId());
                a.setOrderId(tbOrder.getId());
            });
            save(tbOrder);
            orderProductService.saveBatch(orderProducts);
        }else {
            //更新
            List<TbOrderProduct> existOrderProducts = orderProductService.selectOrderProducts(orderParam.getId());
            List<TbOrderProduct> orderProducts = BeanUtil.copyToList(orderParam.getOrderProductList(), TbOrderProduct.class);
            List<TbOrderProduct> updateOrderProducts = orderProducts.stream().filter(a -> a.getId() != null).collect(Collectors.toList());
            List<TbOrderProduct> saveOrderProducts = orderProducts.stream().filter(a -> a.getId() == null).collect(Collectors.toList());
            saveOrderProducts.forEach(a -> {
                a.setId(IdWorker.getId());
                a.setOrderId(orderParam.getId());
            });
            List<Long> updateOrderProductIds = updateOrderProducts.stream().map(TbOrderProduct::getId).collect(Collectors.toList());
            List<Long> deleteOrderProductIds = existOrderProducts.stream()
                    .map(TbOrderProduct::getId)
                    .filter(id -> !updateOrderProductIds.contains(id))
                    .collect(Collectors.toList());
            orderProductService.removeByIds(deleteOrderProductIds);
            orderProductService.saveBatch(saveOrderProducts);
            orderProductService.updateBatchById(updateOrderProducts);
        }
    }

    @Override
    public OrderResult selectOrderDetail(Long orderId) {
        TbOrder tbOrder = getById(orderId);
        if(tbOrder == null) {
            return null;
        }
        OrderResult orderResult = BeanUtil.copyProperties(tbOrder, OrderResult.class);
        List<TbOrderProduct> orderProducts = orderProductService.selectOrderProducts(tbOrder.getId());
        List<OrderProductResult> productResults = BeanUtil.copyToList(orderProducts, OrderProductResult.class);
        orderResult.setOrderProductList(productResults);
        return orderResult;
    }
}
