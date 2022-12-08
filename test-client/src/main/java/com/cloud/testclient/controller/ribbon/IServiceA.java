package com.cloud.testclient.controller.ribbon;

/**
 * @Description
 * @Author HeLiSen
 * @Date 2022/12/8 9:59
 */
public interface IServiceA {
    String sayHello(String name, String xToken, String preAuthCode, String url);
}
