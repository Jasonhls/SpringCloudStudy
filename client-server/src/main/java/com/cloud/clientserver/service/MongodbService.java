package com.cloud.clientserver.service;

import com.cloud.clientserver.pojo.InnerOrder;
import com.cloud.clientserver.pojo.InnerOrderParam;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface MongodbService {

    void saveOrUpdate(List<InnerOrder> list);

    PageInfo<InnerOrder> list(@RequestBody InnerOrderParam param);
}
