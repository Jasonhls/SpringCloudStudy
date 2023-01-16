package com.cloud.clientserver.controller;

import com.cloud.clientserver.pojo.DeserializerDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 何立森
 * @Date: 2023/01/16/16:27
 * @Description:
 */
@RestController
@RequestMapping(value = "/deserializer")
public class DeserializerController {
    @PostMapping(value = "/test")
    public DeserializerDto getDeserializerDto(@RequestBody DeserializerDto dto) {
        return dto;
    }
}
