package com.cloud.shardingjdbc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.shardingjdbc.entity.TbOrderProduct;

import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/09/25/11:39
 * @Description:
 */
public interface OrderProductService extends IService<TbOrderProduct> {
    /**
     * 查询订单商品集合
     * @param orderId 订单id
     * @return 订单商品集合
     */
    List<TbOrderProduct> selectOrderProducts(Long orderId);
    /**
     * 查询订单商品集合
     * @param orderIds 订单id集合
     * @return 订单商品集合
     */
    List<TbOrderProduct> selectOrderProducts(List<Long> orderIds);
}
