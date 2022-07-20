package com.cloud.testclient.controller;

import com.cloud.testclient.pojo.Student;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: helisen
 * @Date 2021/11/11 16:03
 * @Description:
 */
@RestController
@RequestMapping(value = "/hello")
public class HelloController {
    @GetMapping(value = "/sayHello")
    public String getSayHelloResult(HttpServletRequest request, String name) {
        return "hello, " + request.getHeader("token");
    }

    @PostMapping(value = "/getStudent")
    public Student getStudent(String name, Integer age) {
        Student s = new Student();
        s.setName(name);
        s.setAge(age);
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
}
