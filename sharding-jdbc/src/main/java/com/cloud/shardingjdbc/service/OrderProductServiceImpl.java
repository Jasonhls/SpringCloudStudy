package com.cloud.shardingjdbc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.shardingjdbc.entity.TbOrderProduct;
import com.cloud.shardingjdbc.mapper.OrderProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/09/25/11:42
 * @Description:
 */
@Service
public class OrderProductServiceImpl extends ServiceImpl<OrderProductMapper, TbOrderProduct> implements OrderProductService{

    @Override
    public List<TbOrderProduct> selectOrderProducts(Long orderId) {
        QueryWrapper<TbOrderProduct> qw = new QueryWrapper<>();
        qw.lambda().eq(TbOrderProduct::getOrderId, orderId);
        return list(qw);
    }

    @Override
    public List<TbOrderProduct> selectOrderProducts(List<Long> orderIds) {
        QueryWrapper<TbOrderProduct> qw = new QueryWrapper<>();
        qw.lambda().in(TbOrderProduct::getOrderId, orderIds);
        return list(qw);
    }
}
