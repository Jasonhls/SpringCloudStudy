package com.cloud.clientserver.reactor.asynchronize;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/07/31/15:15
 * @Description:
 */
public class CallbackUserService {
    public void getFavorites(Long userId, Callback<List<Long>> callback) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("当前执行用户id为：" + userId);
                List<Long> favorites = Arrays.asList(1L, 2L, 3L);
                callback.onSuccess(favorites);
            } catch (InterruptedException e) {
                callback.onError("读取出现错误");
            }
        }).start();
    }
}
