package com.cloud.clientserver.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.clientserver.mapper.StudentMapper;
import com.cloud.clientserver.param.StudentPageParam;
import com.cloud.clientserver.pojo.Student;
import com.cloud.clientserver.service.StudentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/02/28/16:40
 * @Description:
 */
@Service
@Slf4j
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Override
    public PageInfo<Student> selectList(StudentPageParam param) {
        log.info("入参为：{}", JSONUtil.toJsonStr(param));
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<Student> students = baseMapper.selectStudentList(param);
        if(CollectionUtil.isEmpty(students)) {
            students = Collections.emptyList();
        }
        return PageInfo.of(students);
    }


}
