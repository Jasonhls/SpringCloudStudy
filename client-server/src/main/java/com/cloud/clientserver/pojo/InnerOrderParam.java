package com.cloud.clientserver.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InnerOrderParam {
    private String systemCode;
    private Integer pageSize;
    private Integer pageNum;
    private String orderCode;
    private String customerName;
    private String tel;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
