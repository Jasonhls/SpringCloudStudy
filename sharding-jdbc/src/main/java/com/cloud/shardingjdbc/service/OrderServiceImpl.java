package com.cloud.shardingjdbc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.shardingjdbc.entity.TbOrder;
import com.cloud.shardingjdbc.mapper.OrderMapper;
import com.cloud.shardingjdbc.param.OrderListParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/09/08/17:19
 * @Description:
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, TbOrder> implements OrderService {
    @Override
    public PageInfo<TbOrder> pageList(OrderListParam param) {
        PageHelper.startPage(param);
        QueryWrapper<TbOrder> qw = new QueryWrapper<>();
        qw.lambda().like(StringUtils.isNotBlank(param.getOrderCode()), TbOrder::getOrderCode, param.getOrderCode())
                .orderByDesc(TbOrder::getCreateTime);
        List<TbOrder> list = list(qw);
        return PageInfo.of(list);
    }
}
