package com.cloud.clientserver.bean;

/**
 * @Author: 何立森
 * @Date: 2024/07/29/9:17
 * @Description:
 */
public class Student implements Person{
    @Override
    public String eat(String foodName) {
        return "我是学生，正在吃的食物是：" + foodName;
    }
}
