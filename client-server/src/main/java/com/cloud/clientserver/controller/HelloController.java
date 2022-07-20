package com.cloud.clientserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: helisen
 * @Date 2021/11/11 16:06
 * @Description:
 */
@RestController
@RequestMapping(value = "/hello")
public class HelloController {
    @GetMapping(value = "/sayHello")
    public String sayHello(String name) {
        return "hello, " + name;
    }
}
