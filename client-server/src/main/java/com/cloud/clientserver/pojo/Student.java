package com.cloud.clientserver.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: 何立森
 * @Date: 2023/02/28/16:24
 * @Description:
 */
@TableName("t_student")
@Data
public class Student {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "age")
    private Integer age;
    @TableField("sex")
    private Integer sex;
    @TableField("create_at")
    private Long createAt;
}
