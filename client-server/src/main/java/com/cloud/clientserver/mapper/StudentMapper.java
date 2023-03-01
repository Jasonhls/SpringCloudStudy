package com.cloud.clientserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.clientserver.param.StudentPageParam;
import com.cloud.clientserver.pojo.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/02/28/16:28
 * @Description:
 */
public interface StudentMapper extends BaseMapper<Student> {
    List<Student> selectStudentList(@Param("qo") StudentPageParam param);
}
