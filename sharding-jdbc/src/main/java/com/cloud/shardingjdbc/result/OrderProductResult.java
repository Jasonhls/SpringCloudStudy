package com.cloud.shardingjdbc.result;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: 何立森
 * @Date: 2023/09/25/13:45
 * @Description:
 */
@Data
public class OrderProductResult {
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
     * 商品价格
     */
    private BigDecimal price;
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
}
