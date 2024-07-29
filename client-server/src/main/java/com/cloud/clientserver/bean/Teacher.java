package com.cloud.clientserver.bean;

/**
 * @Author: 何立森
 * @Date: 2024/07/26/11:03
 * @Description:
 */

public class Teacher implements Person {


    @Override
    public String eat(String foodName) {
        return "我是老师，正在吃的食物是：" + foodName;
    }
}
