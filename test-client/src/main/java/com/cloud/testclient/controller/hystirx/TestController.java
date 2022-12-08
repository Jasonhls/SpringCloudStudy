package com.cloud.testclient.controller.hystirx;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/test")
public class TestController implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @GetMapping(value = "/get")
    public String get(String type) {
        List<Person> list = new ArrayList<>();
        String[] beanNamesForType = applicationContext.getBeanNamesForType(Person.class);
        for (String str : beanNamesForType) {
            Person bean = applicationContext.getBean(str, Person.class);
            list.add(bean);
        }
        for (Person p : list) {
            if(p.support(type)) {
                return p.eat();
            }
        }
        return "hello";
    }
}
