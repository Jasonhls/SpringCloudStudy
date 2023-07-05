package com.cloud.seataclient.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: 何立森
 * @Date: 2023/02/28/16:24
 * @Description:
 */
@TableName("t_student")
@Data
public class Order {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableField(value = "order_code")
    private String orderCode;
    @TableField(value = "order_money")
    private BigDecimal orderMoney;
    @TableField(value = "student_id")
    private Long studentId;
}
