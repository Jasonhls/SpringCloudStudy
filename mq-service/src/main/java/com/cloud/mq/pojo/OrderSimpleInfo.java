package com.cloud.mq.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 何立森
 * @Date: 2023/02/22/15:17
 * @Description:
 */
@Data
public class OrderSimpleInfo implements Serializable {
    private String orderId;
    private String name;
    private String desc;
}
