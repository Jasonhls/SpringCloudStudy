package com.cloud.clientserver.reactor;

import reactor.core.publisher.Flux;

/**
 * @Author: 何立森
 * @Date: 2023/07/31/16:51
 * @Description:
 */
public class ReactorUserService {

    //返回是多个，即Flux
    public Flux<Long> getFavorites(Long userId) {
        return Flux.create(sink -> {
            new Thread(() -> {
                //模拟数据库访问时间
                try {
                    Thread.sleep(1000);
                    System.out.println("当前执行用户id为：" + userId);
                    //发布数据
                    sink.next(1L);
                    sink.next(2L);
                    sink.next(3L);
                    //标识发布结束
                    sink.complete();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    sink.error(new Exception("读取出现错误"));
                }
            }).start();
        });
    }
}
