package com.cloud.es.controller;

import com.cloud.es.pojo.Student;
import com.cloud.es.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 何立森
 * @Date: 2023/04/04/15:38
 * @Description:
 */
@RestController
@RequestMapping(value = "/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping(value = "/save")
    public String save(@RequestBody Student student) {
        studentService.saveStudent(student);
        return "save student success";
    }
}
