package com.cloud.clientserver.reactor;

import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 何立森
 * @Date: 2023/07/31/16:55
 * @Description:
 */
public class ReactorFavoriteService {
    private Map<Long, String> names = new HashMap<Long, String>() {{
        put(1L, "football");
        put(2L, "movie");
        put(3L, "film");
    }};

    //返回单个数据，所以是Mono
    public Mono<String> getDetail(Long id) {
        return Mono.create(sink -> {
            new Thread(() -> {
                //模拟数据库访问时间
                try {
                    Thread.sleep(1000);
                    //发布并完成
                    sink.success(names.get(id));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    sink.error(new Exception("读取出现错误"));
                }
            }).start();
        });
    }
}
