package com.cloud.shardingjdbc.param;

import lombok.Data;

/**
 * @Author: 何立森
 * @Date: 2023/09/19/16:17
 * @Description:
 */
@Data
public class OrderListParam {
    private String orderCode;
    private Integer pageNum;
    private Integer pageSize;
}
