package com.cloud.clientserver.controller;

import com.cloud.clientserver.param.StudentPageParam;
import com.cloud.clientserver.pojo.Student;
import com.cloud.clientserver.service.StudentService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: 何立森
 * @Date: 2023/02/28/16:26
 * @Description:
 */
@RestController
@RequestMapping(value = "/student")
public class StudentController {
    @Autowired
    private StudentService studentService;


    @PostMapping(value = "/list")
    public PageInfo<Student> selectList(@RequestBody StudentPageParam param) {
        return studentService.selectList(param);
    }

    @PostMapping(value = "/currentRequestTest")
    public void currentRequestTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        AtomicInteger atomicInteger = new AtomicInteger(1);
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                studentService.currentRequestTest(atomicInteger.getAndIncrement());
            });
        }
    }
}
