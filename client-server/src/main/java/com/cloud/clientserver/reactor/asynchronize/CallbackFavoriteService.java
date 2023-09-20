package com.cloud.clientserver.reactor.asynchronize;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 何立森
 * @Date: 2023/07/31/15:18
 * @Description:
 */
public class CallbackFavoriteService {
    private Map<Long, String> names = new HashMap<Long, String>() {{
        put(1L, "football");
        put(2L, "movie");
        put(3L, "film");
    }};

    public void getDetail(Long id, Callback<String> callback) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                callback.onSuccess(names.get(id));
            } catch (InterruptedException e) {
                callback.onError("读取出现错误");
            }
        }).start();
    }


}
