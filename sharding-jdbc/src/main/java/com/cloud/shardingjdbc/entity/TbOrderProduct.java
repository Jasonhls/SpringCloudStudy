package com.cloud.shardingjdbc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: 何立森
 * @Date: 2023/09/25/11:36
 * @Description:
 */
@Data
public class TbOrderProduct {
    /**
     * 订单商品id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 订单id
     */
    @TableField(value = "order_id")
    private Long orderId;
    /**
     * 商品编码
     */
    @TableField(value = "product_code")
    private String productCode;
    /**
     * 商品名称
      */
    @TableField(value = "product_name")
    private String productName;
    /**
     * 商品数量
     */
    @TableField(value = "num")
    private Integer num;
    /**
     * 商品价格
     */
    @TableField(value = "price")
    private BigDecimal price;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private Long createBy;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 是否删除，0未删除，1删除
     */
    @TableField(value = "is_delete")
    private Boolean isDelete;
}
