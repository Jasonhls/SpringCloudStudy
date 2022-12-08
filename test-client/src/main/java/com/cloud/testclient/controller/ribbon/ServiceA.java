package com.cloud.testclient.controller.ribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @Description
 * @Author HeLiSen
 * @Date 2022/12/8 9:56
 */
@Service
public class ServiceA implements IServiceA{
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String sayHello(String name, String xToken, String preAuthCode, String url) {
        //参数
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("name", name);
        //请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-token", xToken);
        httpHeaders.add("pre-auth-code", preAuthCode);
        //封装请求头
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
        //http://eureka-client/client/hello/sayHello?name=hls
        //http://test-client/test/hello/sayHello?name=hls
        ResponseEntity<String> exchange = restTemplate.exchange(url + "/hello/sayHello?name={name}", HttpMethod.GET, httpEntity, String.class, params);
        return exchange.getBody();
    }

}
