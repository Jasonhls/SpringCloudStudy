package com.cloud.testclient.controller.ribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author HeLiSen
 * @Date 2022/12/8 10:29
 */
@RestController
@RequestMapping(value = "/ribbon")
public class RibbonController {
    @Autowired
    private IServiceA iServiceA;

    @GetMapping(value = "/test")
    public String test(String name, String xToken, String preAuthCode, String url) {
        return iServiceA.sayHello(name, xToken, preAuthCode, url);
    }
}
