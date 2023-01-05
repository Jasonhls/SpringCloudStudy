package com.cloud.clientserver.controller;

import com.cloud.clientserver.pojo.InnerOrder;
import com.cloud.clientserver.service.MongodbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mongodb")
public class MongodbController {
    @Autowired
    private MongodbService mongodbService;

    @PostMapping(value = "/saveOrUpdate")
    public void saveOrUpdate(@RequestBody List<InnerOrder> list) {
        mongodbService.saveOrUpdate(list);
    }
}
