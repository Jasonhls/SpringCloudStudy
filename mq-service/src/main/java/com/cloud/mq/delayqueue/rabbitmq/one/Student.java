package com.cloud.mq.delayqueue.rabbitmq.one;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 何立森
 * @Date: 2023/03/30/14:43
 * @Description:
 */
@Data
public class Student implements Serializable {
    private String name;
    private Integer age;
}
