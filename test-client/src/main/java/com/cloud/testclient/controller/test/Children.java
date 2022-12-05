package com.cloud.testclient.controller.test;

import org.springframework.stereotype.Component;

@Component
public class Children implements Person{
    @Override
    public String eat() {
        return "小孩吃糖果";
    }

    @Override
    public boolean support(String type) {
        return "children".equalsIgnoreCase(type);
    }
}
