package com.cloud.clientserver.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 何立森
 * @Date: 2023/02/20/14:36
 * @Description:
 */
@Data
public class OrderSimpleInfo implements Serializable {
    private String orderId;
    private String name;
    private String desc;
}
