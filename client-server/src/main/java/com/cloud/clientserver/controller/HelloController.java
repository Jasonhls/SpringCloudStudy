package com.cloud.clientserver.controller;

import com.cloud.clientserver.feign.TestFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: helisen
 * @Date 2021/11/11 16:06
 * @Description:
 */
@RestController
@RequestMapping(value = "/hello")
public class HelloController {

    @Autowired
    private TestFeign testFeign;

    @GetMapping(value = "/sayHello")
    public String sayHello(@RequestParam(value = "name") String name) {
        return "hello, " + name;
    }

    @PostMapping(value = "/getString")
    public String getString() {
        //服务之间直接调用
        return testFeign.test();
    }
}
