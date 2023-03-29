package com.cloud.es.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: 何立森
 * @Date: 2023/03/24/18:12
 * @Description:
 */
@Data
@TableName("crm_order_copy")
public class Order implements Serializable {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 主账户ID，即租户公司ID
     */
    @TableField("account_id")
    private Long accountId;

    /**
     * 订单号
     */
    @TableField("order_code")
    private String orderCode;

    /**
     * 订单状态,0待付款、1待发货、2已发货、3待评价、4已完成、5已取消、6已退款
     */
    @TableField("order_status")
    private Byte orderStatus;

    /**
     * 归属人员ID
     */
    @TableField("owner_id")
    private Long ownerId;

    /**
     * 订单提交时间
     */
    @TableField("submit_date")
    private LocalDateTime submitDate;

    /**
     * 提交人
     */
    @TableField("submit_user_name")
    private String submitUserName;

    /**
     * 外部关联订单号
     */
    @TableField("relevance_order_code")
    private String relevanceOrderCode;

    /**
     * 订单来源11珍客商城、500 抖音、2商品图册、3 手动创建、200 有赞 600 其他
     */
    @TableField("order_source")
    private Integer orderSource;

    /**
     * 订单二级来源,201有赞导入、202有赞同步、601其他导入、602其他同步
     */
    @TableField("order_second_source")
    private Integer orderSecondSource;

    /**
     * 收货人手机
     */
    @TableField("receiver_phone")
    private String receiverPhone;

    /**
     * 第三方店铺ID
     */
    @TableField("shop_third_party_id")
    private Integer shopThirdPartyId;

    @TableField("shop_type")
    private Integer shopType;

    /**
     * 物流类型
     */
    @TableField("take_type")
    private Byte takeType;

    @TableField("tel")
    private String tel;


    @TableField("system_type")
    private Integer systemType;

    @TableField("is_delete")
    private Integer isDelete;
}
