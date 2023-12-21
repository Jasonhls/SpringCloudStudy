package com.cloud.clientserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: 何立森
 * @Date: 2023/12/14/9:50
 * @Description:
 */
@RequestMapping("/restTemplate")
@RestController
public class RestTemplateController {
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(value = "/test")
    public Object test() {
        String url = "http://localhost:8893/test/hello/getStudent";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = "{}";
        HttpEntity<Object> request = new HttpEntity<>(json, headers);
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, request, Object.class);
        return responseEntity.getBody();
    }
}
