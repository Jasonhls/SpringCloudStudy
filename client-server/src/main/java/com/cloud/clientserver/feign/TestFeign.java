package com.cloud.clientserver.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Author: 何立森
 * @Date: 2023/01/12/14:27
 * @Description: 通过服务名，直接调用，前提是要同属于同一个注册中心中
 */
@FeignClient(value = "test-client", contextId = "testFeign", path = "/test/hello")
public interface TestFeign {

    @PostMapping(value = "/getStr")
    String test();


}
