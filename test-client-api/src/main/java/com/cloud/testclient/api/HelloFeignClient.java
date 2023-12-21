package com.cloud.testclient.api;

import com.cloud.testclient.req.StudentReq;
import com.cloud.testclient.resp.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "test-client", contextId = "helloFeignClient", path = "/test/hello")
public interface HelloFeignClient {
    @PostMapping(value = "/getStudent")
    Student getStudent(@RequestBody StudentReq studentReq);
}
