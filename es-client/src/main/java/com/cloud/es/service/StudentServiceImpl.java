package com.cloud.es.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.es.mapper.StudentMapper;
import com.cloud.es.pojo.Student;
import org.springframework.stereotype.Service;

/**
 * @Author: 何立森
 * @Date: 2023/03/24/18:30
 * @Description:
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Override
    public void saveStudent(Student student) {
        if(student.getAge() > 200) {
            throw new RuntimeException("年龄超限了");
        }
        save(student);
    }
}
