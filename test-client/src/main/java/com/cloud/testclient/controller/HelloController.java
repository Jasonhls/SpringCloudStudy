package com.cloud.testclient.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.cloud.testclient.api.HelloFeignClient;
import com.cloud.testclient.req.StudentReq;
import com.cloud.testclient.resp.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: helisen
 * @Date 2021/11/11 16:03
 * @Description:
 */
@RestController
@RequestMapping(value = "/hello")
@Slf4j
public class HelloController implements HelloFeignClient {
    @GetMapping(value = "/sayHello")
    public String getSayHelloResult(HttpServletRequest request, String name) {
        return "hello, " + request.getHeader("token");
    }

    @PostMapping(value = "/getStudent")
    public Student getStudent(@RequestBody StudentReq studentReq) {
        log.info("我是端口号为" + SpringUtil.getProperty("server.port") + "的client服务");
        Student s = new Student();
        s.setName(studentReq.getName());
        s.setAge(studentReq.getAge());
        return s;
    }


    @PostMapping(value = "/updateStudent")
    public Student updateStudent(@RequestBody Student student) {
        student.setName("tom");
        student.setAge(30);
        return student;
    }

    @PostMapping(value = "/test")
    public Student test() {

        return null;
    }

    @PostMapping(value = "/getStr")
    public String getStr() {
        log.info("我是端口号为" + SpringUtil.getProperty("server.port") + "的client服务");
        return "hls";
    }
}
