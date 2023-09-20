package com.cloud.clientserver.reactor.asynchronize;

/**
 * @Author: 何立森
 * @Date: 2023/07/31/15:15
 * @Description:
 */
public interface Callback<T> {
    void onSuccess(T t);
    void onError(String error);
}
