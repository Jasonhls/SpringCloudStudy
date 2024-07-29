package com.cloud.clientserver.controller;

import com.cloud.clientserver.bean.Person;
import com.cloud.clientserver.bean.Teacher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: 何立森
 * @Date: 2024/07/26/11:55
 * @Description:
 */
@RestController
@RequestMapping(value = "/play")
public class PlayController {
    @Resource
    private Person abc;

    @GetMapping(value = "/testResource")
    public String test(String foodName) {
        return abc.eat(foodName);
    }
}
