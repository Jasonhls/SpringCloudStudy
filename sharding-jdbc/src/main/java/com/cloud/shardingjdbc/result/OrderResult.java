package com.cloud.shardingjdbc.result;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/09/25/13:44
 * @Description:
 */
@Data
public class OrderResult {
    /**
     * 主键id
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
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 是否删除，0未删除，1删除
     */
    private Boolean isDelete;
    /**
     * 订单商品集合
     */
    private List<OrderProductResult> orderProductList;
}
