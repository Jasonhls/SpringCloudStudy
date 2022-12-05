package com.cloud.testclient.controller.test;

import org.springframework.stereotype.Component;

@Component
public class Women implements Person{
    @Override
    public String eat() {
        return "女人吃甜品";
    }

    @Override
    public boolean support(String type) {
        return "women".equalsIgnoreCase(type);
    }
}
