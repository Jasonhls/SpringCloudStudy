package com.cloud.testclient.controller.test;

import org.springframework.stereotype.Component;

@Component
public class Man implements Person{
    @Override
    public String eat() {
        return "男人吃东西";
    }

    @Override
    public boolean support(String type) {
        return "man".equalsIgnoreCase(type);
    }
}
