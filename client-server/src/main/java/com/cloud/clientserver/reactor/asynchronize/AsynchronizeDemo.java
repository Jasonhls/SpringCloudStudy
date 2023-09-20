package com.cloud.clientserver.reactor.asynchronize;

import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/07/31/15:21
 * @Description:
 */
public class AsynchronizeDemo {
    public static void main(String[] args) throws InterruptedException {
        //Example 1 一个很常见的场景，获取某用户的喜欢栏目，并截取前两个，涉及到两个服务:
        //UserService(根据用户id获取喜欢栏目ids)，FavoriteService(根据栏目id获取栏目详情)
        CallbackUserService userService = new CallbackUserService();
        CallbackFavoriteService favoriteService = new CallbackFavoriteService();
        userService.getFavorites(23L, new Callback<List<Long>>() {
            @Override
            public void onSuccess(List<Long> longs) {
                longs.stream().limit(2).forEach(l -> {
                    favoriteService.getDetail(l, new Callback<String>() {
                        @Override
                        public void onSuccess(String s) {
                            System.out.println(s);
                        }

                        @Override
                        public void onError(String error) {
                            System.out.println(error);
                        }
                    });
                });
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
            }
        });

        while(true) {
            Thread.sleep(10000);
            System.out.println("-------做点其他事-----------");
        }
    }
}
