package com.cloud.clientserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.clientserver.param.StudentPageParam;
import com.cloud.clientserver.pojo.Student;
import com.github.pagehelper.PageInfo;

/**
 * @Author: 何立森
 * @Date: 2023/02/28/16:28
 * @Description:
 */
public interface StudentService extends IService<Student> {
    PageInfo<Student> selectList(StudentPageParam param);

    void insertStudent(Student student);

    void currentRequestTest(int num);
}
