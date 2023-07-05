package com.cloud.es.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.es.pojo.Student;

/**
 * @Author: 何立森
 * @Date: 2023/03/24/18:30
 * @Description:
 */
public interface StudentService extends IService<Student> {
    void saveStudent(Student student);
}
