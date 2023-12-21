package com.cloud.clientserver.controller.loadbalancer;

import com.cloud.testclient.api.HelloFeignClient;
import com.cloud.testclient.req.StudentReq;
import com.cloud.testclient.resp.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 何立森
 * @Date: 2023/12/15/13:52
 * @Description:
 */
@RestController
@RequestMapping("/loadBalancer")
public class LoadBalancerController {
    @Autowired
    private HelloFeignClient helloFeignClient;

    @PostMapping(value = "/test")
    public Student test(@RequestBody StudentReq req) {
        Student student = helloFeignClient.getStudent(req);
        return student;
    }
}
