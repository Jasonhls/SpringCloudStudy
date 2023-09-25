package com.cloud.shardingjdbc.param;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: 何立森
 * @Date: 2023/09/25/11:27
 * @Description:
 */
@Data
public class OrderProductParam {
    /**
     * 订单商品id
     */
    private Long id;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 商品编码
     */
    private String productCode;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品数量
     */
    private Integer num;
    /**
     * 订单商品价格
     */
    private BigDecimal price;
}
