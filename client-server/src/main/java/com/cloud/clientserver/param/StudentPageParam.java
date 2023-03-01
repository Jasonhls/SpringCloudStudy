package com.cloud.clientserver.param;

import lombok.Data;

/**
 * @Author: 何立森
 * @Date: 2023/02/28/16:45
 * @Description:
 */
@Data
public class StudentPageParam {
    private Integer pageNum;
    private Integer pageSize;
    private String name;
    private Long id;
}
