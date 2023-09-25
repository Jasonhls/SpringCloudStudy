package com.cloud.shardingjdbc.param;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/09/25/11:26
 * @Description:
 */
@Data
public class OrderParam {
    /**
     * 订单id
     */
    private Long id;
    /**
     * 订单编号
     */
    private String orderCode;
    /**
     * 订单金额
     */
    private BigDecimal orderMoney;
    /**
     * 下单人手机号
     */
    private String tel;
    /**
     * 收货地址
     */
    private String address;
    /**
     * 订单商品集合
     */
    private List<OrderProductParam> orderProductList;
}
