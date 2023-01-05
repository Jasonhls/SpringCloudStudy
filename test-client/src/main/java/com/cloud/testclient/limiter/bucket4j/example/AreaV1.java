package com.cloud.testclient.limiter.bucket4j.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaV1 {
    private String name;
    private Integer length;
    private Integer width;
}
