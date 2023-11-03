package com.cloud.clientserver.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.clientserver.mapper.StudentMapper;
import com.cloud.clientserver.param.StudentPageParam;
import com.cloud.clientserver.pojo.Student;
import com.cloud.clientserver.service.StudentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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

    @Autowired
    private RedissonClient redissonClient;

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

    @Override
    public void insertStudent(Student student) {
        baseMapper.insert(student);
    }

    /**
     * 锁加锁解锁，然后多并发场景下，前一个事务还没有提交，后一个请求过来了，开启新事务中没法读取到前一个事务中还没有提交的数据。
     * @param num
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void currentRequestTest(int num) {
        RLock lock = redissonClient.getLock("哈哈");
        try {
            log.info("加锁了");
            lock.lock();
            selectStudent(num);
            Student s = new Student();
            s.setName("编号");
            s.setAge(num);
            s.setSex(1);
            insertStudent(s);
        }finally {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    log.info("释放锁，状态为{}", status);
                    lock.unlock();
                }
            });
        }
    }

    private void selectStudent(int age) {
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.lambda().eq(Student::getName, "编号")
                .le(Student::getAge, age)
                .orderByDesc(Student::getAge);
        List<Student> list = list(qw);
        if(CollectionUtil.isEmpty(list)) {
            log.info("查询学生结果为空");
        }else {
            log.info("查询到的学生为{}", JSONUtil.toJsonStr(list.get(0)));
        }
    }
}
