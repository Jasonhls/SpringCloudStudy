package com.cloud.shardingjdbc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: 何立森
 * @Date: 2023/09/08/17:08
 * @Description:
 */
@Data
public class TbOrder {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 订单编号
     */
    @TableField(value = "order_code")
    private String orderCode;
    /**
     * 订单金额
     */
    @TableField(value = "order_money")
    private BigDecimal orderMoney;
    /**
     * 下单人手机号
     */
    @TableField(value = "tel")
    private String tel;
    /**
     * 收货地址
     */
    @TableField(value = "address")
    private String address;
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
